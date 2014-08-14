/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 12 21, 11, 6:26:00 PM
 */
package main.view;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import main.model.*;

/**
 *
 * @author Aaron
 */
public final class MainFrame extends javax.swing.JFrame
{
    private final CardLayout cardLayout = new CardLayout();
    private ViewPanel view = null;
    private AddPanel add = null;
    private JTextField searchTextField;
    private VocabularyRecord vRecord = null;
    
    private static String selectedForeignLanguage = ForeignLanguage.HOKKIEN.toString();
    private final static ApplicationLock appLock = new ApplicationLock();      

    private final LogManager logger = LogManager.getInstance();
    
    /**
     * Sets up the background image, Layout, and actionListeners.  
     */        
    public MainFrame() 
    {   
        try
        {
            this.vRecord = new VocabularyRecord();
        }
        catch(final SQLException ex)
        {
            this.logger.error(ex.toString());
        }
        
        appLock.lockApplication("Vocabulary is already running.");
        Runtime.getRuntime().addShutdownHook(new ShutDownHookHandler(appLock, this.vRecord));         

        this.setIconImage( new ImageIcon(getClass().getResource("/images/icon.png")).getImage() );

        this.initComponents();
        this.setSize(891,561);
   
        this.view = new ViewPanel(this.vRecord); 
        this.add = new AddPanel(this.vRecord);

        this.mainPanel.setLayout(this.cardLayout); 
        this.mainPanel.add(add, "Add");
        this.mainPanel.add(view, "View");
        
        this.searchTextField.setVisible(false);    
        this.suggestListScrollPane.setVisible(false); 

        for(ForeignLanguage language: ForeignLanguage.values())
        {
            this.foreignLanguageComboBox.addItem(language.toString());
        }
        
        this.addListeners();
    }

    /**
     * Returns the selected language.
     * @return a String.
     */
    public static String getforeignLanguage()
    {
        return selectedForeignLanguage;
    }
    
    // adds event listener to all mainFrame's component.
    private void addListeners()
    {   
        this.addButton.addActionListener(new ActionListener()    
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    cardLayout.show(mainPanel, "Add");
                    searchTextField.setVisible(false); 
                    searchTextField.setText("");
                    suggestListScrollPane.setVisible(false); 
                }
            });
        
        this.viewButton.addActionListener(new ActionListener()    
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    cardLayout.show(mainPanel, "View");
                    searchTextField.setVisible(true); 
                    add.clearTextfields();
                    view.refreshTable();
                }
            });

        this.foreignLanguageComboBox.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    add.setLanguageLabel( foreignLanguageComboBox.getSelectedItem().toString() );
                    view.changeSecondColumnHeaderName( foreignLanguageComboBox.getSelectedItem().toString() );

                    selectedForeignLanguage =  foreignLanguageComboBox.getSelectedItem().toString(); 
                    view.refreshTable();
                }
            });  
        
        final SuggestionListListener suggestionListListener = new SuggestionListListener();
        
        this.suggestionList.addMouseListener(suggestionListListener);
        this.suggestionList.addMouseMotionListener(suggestionListListener); 
        
        final SearchTextFieldListener searchTextFieldListener = new SearchTextFieldListener();

        this.searchTextField.addKeyListener(searchTextFieldListener);
        this.searchTextField.addCaretListener(searchTextFieldListener);
        this.searchTextField.addFocusListener(searchTextFieldListener);
        
        this.backupButton.addActionListener(new BackupListener()); 
        
        this.showLogsMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    final LogFrame logFrame = new LogFrame();
                    logFrame.setLocationRelativeTo(MainFrame.this);
                    logFrame.setVisible(true);
                }
            });
    }
    
    /**
     * Initializes all GUI. Generated by NetBeans.      
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        addButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        suggestListScrollPane = new javax.swing.JScrollPane();
        suggestionList = new javax.swing.JList<>();
        foreignLanguageComboBox = new javax.swing.JComboBox<>();
        backupButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        mainPanel = new javax.swing.JPanel();
        background = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        viewMenu = new javax.swing.JMenu();
        showLogsMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vocabulary");
        setResizable(false);
        getContentPane().setLayout(null);

        addButton.setText("Add");
        jLayeredPane1.add(addButton);
        addButton.setBounds(40, 40, 110, 60);

        viewButton.setText("View");
        jLayeredPane1.add(viewButton);
        viewButton.setBounds(40, 140, 110, 60);

        suggestionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setVisibleRowCount(7);
        suggestListScrollPane.setViewportView(suggestionList);

        jLayeredPane1.add(suggestListScrollPane);
        suggestListScrollPane.setBounds(10, 330, 170, 120);

        jLayeredPane1.add(foreignLanguageComboBox);
        foreignLanguageComboBox.setBounds(40, 240, 110, 20);
        try
        {
            URL url = MainFrame.class.getResource("/images/search_icon.png");
            final BufferedImage searchIcon = ImageIO.read(url);
            searchTextField = new javax.swing.JTextField()
            {
                protected void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    int y = (getHeight() - searchIcon.getHeight())/2;
                    g.drawImage(searchIcon, 1, y, this);
                }
            };
            searchTextField.setMargin(new Insets(0, searchIcon.getWidth(), 0, 0));
        }catch(IOException e){}
        searchTextField.setBounds(10, 305, 170, 25);
        jLayeredPane1.add(searchTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        backupButton.setFont(ViewManager.VERDANA12);
        backupButton.setText("Create Backup");
        backupButton.setOpaque(false);
        jLayeredPane1.add(backupButton);
        backupButton.setBounds(30, 360, 130, 23);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jLayeredPane1.add(jSeparator1);
        jSeparator1.setBounds(200, 0, 10, 510);

        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        jLayeredPane1.add(mainPanel);
        mainPanel.setBounds(200, 0, 690, 510);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background.png"))); // NOI18N
        jLayeredPane1.add(background);
        background.setBounds(0, 0, 890, 510);

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(0, 0, 900, 510);

        viewMenu.setText("  View  ");

        showLogsMenuItem.setText("    Show Logs    ");
        showLogsMenuItem.setToolTipText("");
        viewMenu.add(showLogsMenuItem);

        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Suggestion list mouse listener class.
     */
    private  class SuggestionListListener extends MouseAdapter
    {
        /**
         * Highlights the text in the suggestion field where the mouse is pointing at.
         * @param e mouse event
         */
        @Override
        public void mouseMoved(final MouseEvent e)
        {
            final int index = suggestionList.locationToIndex( e.getPoint() ); // get the index of the word in the list where the pointer is pointing

            suggestionList.setSelectedIndex(index); 
        }
        
        /**
         * Gets the selected text from the suggestion list and replace the text in search bar.
         * @param e mouse event
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
            if( suggestionList.getSelectedValue() != null )
            {
                searchTextField.setText( suggestionList.getSelectedValue().toString() );
                
                requestFocusInWindow();
            }              
        }
    }
    
    /**
     * Search textfield 
     */
    private class SearchTextFieldListener extends KeyAdapter implements CaretListener, FocusListener
    {
        public static final int SUGGESTION_LIST_ROW_HEIGHT = 22;
        private int[] searchWordIndex;
        private int searchWordCounter = 0;

        /**
         * Suggests words similar to currently typed text.
         * @param e caret event
         */
        @Override
        public void caretUpdate(final CaretEvent e)
        {
            if( searchTextField.getText().isEmpty() )
            {
                suggestListScrollPane.setVisible(false);
            } 
            else
            {                               
                final String searchedWord = searchTextField.getText();

                suggestListScrollPane.setVisible(true); 

                final List<List<?>> suggestionsAndSearchedWordList = this.getSuggestionsAndSearchedWord(searchedWord);
                final List<?> suggestionsList = suggestionsAndSearchedWordList.get(0);
                final String[] suggestionsArray = suggestionsList.toArray(new String[suggestionsList.size()]);

                suggestionList.setListData( suggestionsArray ); // fills the suggestion box

                final List<?> searchedWordList = suggestionsAndSearchedWordList.get(1);
                
                if(searchedWordList.isEmpty() == false)
                {
                    view.getVocabularyTable().changeSelection( (Integer)searchedWordList.get(0), 0, false, false); 
                }

                // Used by keylistener "enter", for multiple identical search word results.
                this.searchWordIndex = new int[searchedWordList.size()];
                for(int i = 0; i < this.searchWordIndex.length; i++)
                {
                    this.searchWordIndex[i] = (Integer)searchedWordList.get(i);
                }

                // only one suggested text left and a row is selected.
                if(suggestionList.getModel().getSize() <= 0 &&
                   view.getVocabularyTable().getSelectedRow() >= 1)
                {
                    suggestListScrollPane.setVisible(false);
                } 
                else // automatically change suggestion list height depending on number of data.
                {
                    final int listHeight = suggestionList.getModel().getSize() * SUGGESTION_LIST_ROW_HEIGHT; // 22 is the height of each row
                    if(listHeight > 143)   
                    {
                        suggestListScrollPane.setSize(170, 143); // default 7 rows
                    }
                    else
                    {
                        suggestListScrollPane.setSize(170, listHeight);
                    }
                    
                    suggestListScrollPane.setVisible(true);
   
                    if(searchedWord.length() <= 1)
                    {
                        // deselects selected row/s in the table.
                        view.getVocabularyTable().removeRowSelectionInterval(0, view.getVocabularyTable().getRowCount() - 1);
                    }
                } 
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
            final int arrayALength = arrayA.length;
            final int arrayBLength = arrayB.length;
            final String[] concatenatedArray = new String[arrayALength + arrayBLength];

            System.arraycopy(arrayA, 0, concatenatedArray, 0, arrayALength);
            System.arraycopy(arrayB, 0, concatenatedArray, arrayALength, arrayBLength);
            
            return concatenatedArray;
        }
        
        private List<List<?>> getSuggestionsAndSearchedWord(final String keyWord)
        {
            final int numOfRows = view.getVocabularyTable().getRowCount();
            final List<String> suggestions = new ArrayList<>();

            String tableRow[];
            
            int i;
            final List<Integer> searchWordIndexList = new ArrayList<>();

            for(i = 0; i < numOfRows; i++)
            {
                if(keyWord.equals("")) 
                {
                    break;
                }

                tableRow = this.concatenateArrays(view.getVocabularyTable().getValueAt(i, 0).toString().split(" / "),
                                                  view.getVocabularyTable().getValueAt(i, 1).toString().split(" / "));

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
            final Collator collator = Collator.getInstance(Locale.US);
            Collections.sort(suggestions, collator);

            final List<List<?>> suggestionsAndSearchWordIndex = new ArrayList<>(2);
            suggestionsAndSearchWordIndex.add(suggestions);
            suggestionsAndSearchWordIndex.add(searchWordIndexList);
            
            return suggestionsAndSearchWordIndex; 
        }
        
        /**
          Moves selected word in suggest list via up-down arrow keys and enter key.
          @param e key event
        */
        @Override
        public void keyPressed(final KeyEvent e)
        {           
            final int keycode = e.getKeyCode();
            final int selectedIndex = suggestionList.getSelectedIndex();

            if( keycode == KeyEvent.VK_UP )
            {                     
                if( selectedIndex > -1 ) // there is a selected word in the list.
                {
                    if( selectedIndex  == 0 ) //if at the beginning of the list, move back to end.  
                    {
                        suggestionList.setSelectedIndex(suggestionList.getModel().getSize() - 1);
                        suggestionList.ensureIndexIsVisible(suggestionList.getModel().getSize() - 1); 
                    }               
                    else
                    {
                        suggestionList.setSelectedIndex(selectedIndex - 1);
                        suggestionList.ensureIndexIsVisible(selectedIndex - 1); 
                    }             
                }
                else
                {
                    suggestionList.setSelectedIndex(suggestionList.getModel().getSize() - 1);
                    suggestionList.ensureIndexIsVisible(suggestionList.getModel().getSize() - 1); 
                } 

            }
            else if( keycode == KeyEvent.VK_DOWN )
            {
                if( selectedIndex > -1 ) // there is a selected word in the list.
                {
                    if( selectedIndex  == suggestionList.getModel().getSize() - 1 ) //if at the end of the list, move back to beginning.
                    {
                        suggestionList.setSelectedIndex(0);
                        suggestionList.ensureIndexIsVisible(0); 
                    } 
                    else
                    {
                        suggestionList.setSelectedIndex(selectedIndex + 1);
                        suggestionList.ensureIndexIsVisible(selectedIndex + 1); 
                    } 
                }
                else
                {
                    suggestionList.setSelectedIndex(0);
                    suggestionList.ensureIndexIsVisible(0); 
                }
            }
            else if( keycode == KeyEvent.VK_ENTER )
            {
                if( suggestionList.getSelectedValue() != null )
                {
                    searchTextField.setText( suggestionList.getSelectedValue().toString() );
                } 

                /* 
                Adds one to the counter to get to the next matched identical searched word.
                Modulo the counter to the length of the searchedWord array to reset the counter and prevent indexOutOfBoundsException.
                */
                if(this.searchWordIndex != null && this.searchWordIndex.length > 0)
                {                              
                    searchWordCounter = (searchWordCounter + 1) % this.searchWordIndex.length;
                    view.getVocabularyTable().changeSelection( this.searchWordIndex[searchWordCounter], 0, false, false); 
                }
            } 
        }

        @Override
        public void focusGained(FocusEvent e)
        {}
              
        /**
          Action event for search textfield. Hides suggestion list if not focused on textfield.
          @param e focus event
        */
        @Override
        public void focusLost(final FocusEvent e)
        {
            suggestListScrollPane.setVisible(false);
        }
    }
    
    private class BackupListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            final DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            final Calendar cal = Calendar.getInstance();
            final String currentDate = dateFormat.format(cal.getTime());
            final String backupScript = "mysqldump --routines -uroot -p10906657 --add-drop-database -B my_vocabulary -r \"my_vocabulary (" + currentDate + ").sql\"";
            final String path = "C://My Application";
            
            final CommandLineScript cmdScript = new CommandLineScript();
            final boolean result = cmdScript.execute(backupScript, path);

            if(true == result)
            {
                JOptionPane.showMessageDialog(MainFrame.this, "Backup created successfully", "", JOptionPane.INFORMATION_MESSAGE);
            }    
            else
            {
                JOptionPane.showMessageDialog(MainFrame.this, cmdScript.getErrorMessage(), "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel background;
    private javax.swing.JButton backupButton;
    private javax.swing.JComboBox<String> foreignLanguageComboBox;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem showLogsMenuItem;
    private javax.swing.JScrollPane suggestListScrollPane;
    private javax.swing.JList<String> suggestionList;
    private javax.swing.JButton viewButton;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}
