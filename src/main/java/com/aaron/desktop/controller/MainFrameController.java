package com.aaron.desktop.controller;

import com.aaron.desktop.model.db.Vocabulary;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import com.aaron.desktop.model.db.VocabularyRecord;
import com.aaron.desktop.model.log.LogManager;
import com.aaron.desktop.model.others.CommandLineScript;
import com.aaron.desktop.model.others.Mailer;
import com.aaron.desktop.view.AboutFrameView;
import com.aaron.desktop.view.LogFrameView;
import com.aaron.desktop.view.MainFrameView;
import static com.aaron.desktop.view.MainFrameView.PanelName.Add;
import static com.aaron.desktop.view.MainFrameView.PanelName.View;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class MainFrameController
{
    private final MainFrameView view;
    private final VocabularyRecord model;
    private final Mailer mailer;

    public MainFrameController(final MainFrameView view, final VocabularyRecord model, final Mailer mailer)
    {
        this.view = view;
        this.model = model;
        this.mailer = mailer;
    }
    
    // adds event listener to all mainFrame's component.
    public void addListeners()
    {   
        this.view.addAddButtonListener(
            (ActionEvent e) ->
            {
                view.showPanel(Add);
                view.getSearchTextField().setVisible(false);
                view.getSearchTextField().setText("");
                view.getSuggestListScrollPane().setVisible(false);
            });
        
        this.view.addViewButtonListener(
            (ActionEvent e) ->
            {    
                List<Vocabulary> vocabList = model.getVocabularies(view.getViewPanelView().getLetterComboBoxItem());

                if(vocabList.isEmpty())
                {
                    JOptionPane.showMessageDialog(view, "Unable to access database", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    view.getViewPanelView().refreshTable(vocabList);view.showPanel(View);
                    view.getSearchTextField().setVisible(true);
                    view.getAddPanelView().clearTextfields();
                }
            });

        this.view.addForeignLanguageComboBoxListener(
            (ActionEvent e) ->
            {
                view.getAddPanelView().setLanguageLabel(view.getForeignLanguageComboBoxItem());

                view.getViewPanelView().changeSecondColumnHeaderName(view.getForeignLanguageComboBoxItem());

                MainFrameView.setForeignLanguage(view.getForeignLanguageComboBoxItem());
                view.getViewPanelView().refreshTable(model.getVocabularies(view.getViewPanelView().getLetterComboBoxItem()));
            });  

        this.view.addSuggestionListListener(new SuggestionListListener(this.view));
        this.view.addSearchTextFieldListener(new SearchTextFieldListener(this.view));
        this.view.addBackupButtonListener(new BackupListener(this.view, this.mailer));
        
        this.view.addAboutMenuItemListener(
            (ActionEvent e) ->
            {
                AboutFrameView aboutFrame = new AboutFrameView();
                aboutFrame.setLocationRelativeTo(view);
                aboutFrame.setVisible(true);
            });
        
        this.view.addShowLogsMenuItemListener(
            (ActionEvent e) ->
            {
                LogFrameView logFrame = new LogFrameView();
                LogManager logger = LogManager.getInstance();
                LogFrameController logFrameController = new LogFrameController(logFrame, logger);

                logFrameController.addListeners();
                logFrame.setLocationRelativeTo(view);
                logFrame.setVisible(true);
            });
    }
    
    /**
     * Suggestion list mouse listener class.
     */
    private static class SuggestionListListener extends MouseAdapter
    {
        private final MainFrameView view;

        public SuggestionListListener(final MainFrameView view)
        {
            this.view = view;
        }

        /**
         * Highlights the text in the suggestion field where the mouse is pointing at.
         * @param e mouse event
         */
        @Override
        public void mouseMoved(final MouseEvent e)
        {
            int index = this.view.getSuggestionList().locationToIndex(e.getPoint()); // get the index of the word in the list where the pointer is pointing

            this.view.getSuggestionList().setSelectedIndex(index); 
        }
        
        /**
         * Gets the selected text from the suggestion list and replace the text in search bar.
         * @param e mouse event
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
            if(this.view.getSuggestionList().getSelectedValue() != null)
            {
                this.view.getSearchTextField().setText(this.view.getSuggestionList().getSelectedValue());
                this.view.getSearchTextField().requestFocusInWindow();
            }              
        }
    }
    
    /**
     * Search text field key, caret, and focus listener class
     */
    private static class SearchTextFieldListener extends KeyAdapter implements CaretListener, FocusListener
    {
        public static final int SUGGESTION_LIST_ROW_HEIGHT = 22;
        private int[] searchWordIndex;
        private int searchWordCounter;
        private final MainFrameView view;

        private final JTextField searchTextField;
        private final JScrollPane suggestListScrollPane;
        private final JList<String> suggestionList;
        private final JTable vocabularyTable;

        public SearchTextFieldListener(final MainFrameView view)
        {
            this.view = view;
            
            searchTextField = this.view.getSearchTextField();
            suggestListScrollPane = this.view.getSuggestListScrollPane();
            suggestionList = this.view.getSuggestionList();
            vocabularyTable = this.view.getViewPanelView().getVocabularyTable();
        }

        /**
         * Suggests words similar to currently typed text.
         * @param e caret event
         */
        @Override
        public void caretUpdate(final CaretEvent e)
        {           
            if(this.searchTextField.getText().isEmpty())
            {
                this.suggestListScrollPane.setVisible(false);
            } 
            else
            {
                String searchedWord = this.searchTextField.getText();

                this.suggestListScrollPane.setVisible(true); 

                List<List<?>> suggestionsAndSearchedWordList = this.getSuggestionsAndSearchedWord(searchedWord);
                List<String> suggestionsList = (List<String>) suggestionsAndSearchedWordList.get(0);
                String[] suggestionsArray = suggestionsList.toArray(new String[suggestionsList.size()]);

                this.suggestionList.setListData(suggestionsArray); // fills the suggestion box

                List<Integer> searchedWordList = (List<Integer>) suggestionsAndSearchedWordList.get(1);
                
                if(searchedWordList.isEmpty())
                {
                    this.vocabularyTable.changeSelection(searchedWordList.get(0), 0, false, false); 
                }

                // Used by keylistener "enter", for multiple identical search word results.
                this.searchWordIndex = new int[searchedWordList.size()];
                for(int i = 0; i < this.searchWordIndex.length; i++)
                {
                    this.searchWordIndex[i] = searchedWordList.get(i);
                }

                this.updateSuggestionListHeight(searchedWord);
            }
        }
        /**
         * Concatenates the two given String arrays
         * @param arrayA String array
         * @param arrayB String array
         * @return array containing contents of arrayA and arrayB
         */
        private String[] concatenateArrays(final String[] arrayA, final String[] arrayB)
        {
            int arrayALength = arrayA.length;
            int arrayBLength = arrayB.length;
            String[] concatenatedArray = new String[arrayALength + arrayBLength];

            System.arraycopy(arrayA, 0, concatenatedArray, 0, arrayALength);
            System.arraycopy(arrayB, 0, concatenatedArray, arrayALength, arrayBLength);
            
            return concatenatedArray;
        }
        
        /**
         * Gets the suggestion list and the list of index of exact matched words.
         * @param the search key word
         * @return a list containing two lists, first one is the suggestions, second one the the index of exact matched.
         */
        private List<List<?>> getSuggestionsAndSearchedWord(final String keyWord)
        {
            int numOfRows = this.vocabularyTable.getRowCount();
            List<String> suggestions = new ArrayList<>();
            String tableRow[];
            List<Integer> searchWordIndexList = new ArrayList<>();

            for(int i = 0; i < numOfRows; i++)
            {
                if(keyWord.isEmpty()) 
                {
                    break;
                }

                tableRow = this.concatenateArrays(this.vocabularyTable.getValueAt(i, 0).toString().split(" / "),
                                                  this.vocabularyTable.getValueAt(i, 1).toString().split(" / "));

                for(String word: tableRow)
                {
                    if(word.startsWith(keyWord))
                    {
                        suggestions.add(word);
                    }

                    if(word.equalsIgnoreCase(keyWord))
                    {
                        searchWordIndexList.add(i);
                        break;
                    }
                }
            }   

            // sorts the list including accented characters
            Collator collator = Collator.getInstance(Locale.US);
            Collections.sort(suggestions, collator);

            List<List<?>> suggestionsAndSearchWordIndex = new ArrayList<>(2);
            suggestionsAndSearchWordIndex.add(suggestions);
            suggestionsAndSearchWordIndex.add(searchWordIndexList);
            
            return suggestionsAndSearchWordIndex; 
        }
        
        /**
         * Updates the suggestion list height depending on the searched word.
         * @param searchedWord the word inputted in the search text field
         */
        private void updateSuggestionListHeight(final String searchedWord)
        {
            // only one suggested text left and a row is selected.
            if(this.suggestionList.getModel().getSize() <= 1 &&
               this.vocabularyTable.getSelectedRow() >= 1)
            {
                this.suggestListScrollPane.setVisible(false);
            }
            else // automatically change suggestion list height depending on number of data.
            {
                int listHeight = (this.suggestionList.getModel().getSize()) * SUGGESTION_LIST_ROW_HEIGHT; // 22 is the height of each row

                if(this.suggestListScrollPane.getHorizontalScrollBar().isVisible())
                {
                    listHeight += 17; // add height for scroll bar
                }

                if(listHeight > 143)
                {
                    this.suggestListScrollPane.setSize(170, 143); // default 7 rows
                }
                else
                {
                    this.suggestListScrollPane.setSize(170, listHeight);
                }

                this.suggestListScrollPane.setVisible(true);

                if(searchedWord.length() <= 1)
                {
                    // deselects selected row/s in the table.
                    this.vocabularyTable.removeRowSelectionInterval(0, this.vocabularyTable.getRowCount() - 1);
                }
            }
        }
        
        /**
         * Moves selected word in suggest list via up-down arrow keys and enter key.
         * @param e key event
         */
        @Override
        public void keyPressed(final KeyEvent e)
        {           
            int keycode = e.getKeyCode();
            int selectedIndex = this.suggestionList.getSelectedIndex();

            switch(keycode)
            {
                case KeyEvent.VK_BACK_SPACE:
                {
                    this.updateSuggestionListHeight(this.searchTextField.getText());
                    break;
                }
                case KeyEvent.VK_UP:
                {
                    if( selectedIndex > -1 ) // there is a selected word in the list.
                    {
                        if( selectedIndex  == 0 ) //if at the beginning of the list, move back to end.
                        {
                            this.suggestionList.setSelectedIndex(this.suggestionList.getModel().getSize() - 1);               
                            this.suggestionList.ensureIndexIsVisible(this.suggestionList.getModel().getSize() - 1);
                        }
                        else
                        {
                            this.suggestionList.setSelectedIndex(selectedIndex - 1);
                            this.suggestionList.ensureIndexIsVisible(selectedIndex - 1);
                        }
                    }
                    else
                    {
                        this.suggestionList.setSelectedIndex(this.suggestionList.getModel().getSize() - 1);
                        this.suggestionList.ensureIndexIsVisible(this.suggestionList.getModel().getSize() - 1);
                    }
                    break;
                }
                case KeyEvent.VK_DOWN:
                {
                    if( selectedIndex > -1 ) // there is a selected word in the list.
                    {
                        if( selectedIndex  == this.suggestionList.getModel().getSize() - 1 ) //if at the end of the list, move back to beginning.
                        {
                            this.suggestionList.setSelectedIndex(0); 
                            this.suggestionList.ensureIndexIsVisible(0);
                        }
                        else
                        {
                            this.suggestionList.setSelectedIndex(selectedIndex + 1);
                            this.suggestionList.ensureIndexIsVisible(selectedIndex + 1);
                        }
                    }
                    else
                    {
                        this.suggestionList.setSelectedIndex(0);
                        this.suggestionList.ensureIndexIsVisible(0);
                    }
                    break;
                }
                case KeyEvent.VK_ENTER:
                {
                    if(this.suggestionList.getSelectedValue() != null)
                    {
                        this.searchTextField.setText(this.suggestionList.getSelectedValue());
                    }
                    /* 
                      Adds one to the counter to get to the next matched identical searched word.
                      Modulo the counter to the length of the searchedWord array to reset the counter and prevent indexOutOfBoundsException.
                    */
                    if(this.searchWordIndex != null && this.searchWordIndex.length > 0)
                    {
                        this.searchWordCounter = (searchWordCounter + 1) % this.searchWordIndex.length;
                        this.vocabularyTable.changeSelection(this.searchWordIndex[searchWordCounter], 0, false, false);
                    }
                    break;
                }
                default:
                    // Do nothing
            }
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            // No action
        }
              
        /**
          Action event for search textfield. Hides suggestion list if not focused on textfield.
          @param e focus event
        */
        @Override
        public void focusLost(FocusEvent e)
        {
            this.suggestListScrollPane.setVisible(false);
        }
    }
    
    /**
     * Backup button listener class
     */
    private static class BackupListener implements ActionListener
    {
        private final MainFrameView view;
        private final Mailer mailer;

        public BackupListener(final MainFrameView view, final Mailer mailer)
        {
            this.view = view;
            this.mailer = mailer;
        }

        /**
         * Creates a backup of the MySQL database.
         * @param e action event
         */
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar cal = Calendar.getInstance();
            String currentDate = dateFormat.format(cal.getTime());
            String fileName = "my_vocabulary (" + currentDate + ").sql";
            String backupScript = "mysqldump --routines -uroot -proot --add-drop-database -B my_vocabulary -r \"" + fileName + "\"";
            String path = "./backup";
            File dir = new File(path);

            if(!dir.exists())
            {
                dir.mkdir();
            }

            CommandLineScript cmdScript = new CommandLineScript();
            boolean success = cmdScript.execute(backupScript, path);

            int result = JOptionPane.showConfirmDialog(this.view, "Do you want to email the backup?", "Email Confirmation",
                                                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(success)
            {
                JOptionPane.showMessageDialog(this.view, "Backup created successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
            }    
            else
            {
                JOptionPane.showMessageDialog(this.view, cmdScript.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            if(result == JOptionPane.YES_OPTION)
            {
                success = this.mailer.sendMail("Vocabulary backup " + currentDate, " ", path + "/" + fileName);
                
                if(success)
                {
                    JOptionPane.showMessageDialog(this.view, "Backup sent to mail", "Info", JOptionPane.INFORMATION_MESSAGE);
                }    
                else
                {
                    JOptionPane.showMessageDialog(this.view, this.mailer.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
