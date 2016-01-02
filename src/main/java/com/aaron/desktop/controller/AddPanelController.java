/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aaron.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import com.aaron.desktop.model.db.Vocabulary;
import com.aaron.desktop.model.db.VocabularyRecord;
import com.aaron.desktop.view.AddPanelView;
import com.aaron.desktop.view.MainFrameView;

/**
 *
 * @author Aaron
 */
public class AddPanelController
{
    private final AddPanelView view;
    private final VocabularyRecord model;
    
    public AddPanelController(final AddPanelView view, final VocabularyRecord model)
    {
        this.view = view;
        this.model = model;
    }
    
    /**
     * Adds event listener to all components under this controller's view.
     */
    public void addListeners()
    {
        this.view.addAccentedButtonsListener(new AccentedButtonListener(this.view));

        this.view.addAddButtonListener(
            (ActionEvent e) ->
            {
                insertToDatabase();
            });

        KeyAdapter fieldAddButtonListener = new KeyAdapter() 
            {
                @Override
                public void keyPressed(final KeyEvent e) 
                {
                    if(KeyEvent.VK_ENTER == e.getKeyCode())
                    {
                        insertToDatabase();
                    }
                }
            };

        this.view.addEnglishTextFieldListener(fieldAddButtonListener);
        this.view.addForeignTextFieldListener(fieldAddButtonListener,
            (CaretEvent ce) -> 
            {
                view.setCurrentCaretPosition(ce.getDot());
            });
    }
    
        
    /**
     * Adds the data to the database.
     */
    private void insertToDatabase()
    {
        if(this.view.isTextFieldsEmpty())
        {
            JOptionPane.showMessageDialog(this.view, "Complete all fields.", "Note", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            boolean success = this.model.addToDatabase(new Vocabulary(this.view.getEnglishTextField().getText().trim(), 
                                                                      this.view.getForeignTextField().getText().trim(), 
                                                                      MainFrameView.getforeignLanguage()));
            if(success)
            {
                this.view.clearTextfields();
                this.view.getEnglishTextField().requestFocusInWindow();
                JOptionPane.showMessageDialog(this.view, "Added.", "Note", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Inner class for accented button action.
     */
    private static class AccentedButtonListener implements ActionListener
    {
        private final AddPanelView addPanel;
        
        public AccentedButtonListener(final AddPanelView addPanel)
        {
            this.addPanel = addPanel;
        }
        
        /**
         * Inserts the clicked accented character into the current caret position of the foreign text field.
         * @param e action event of JButton
         */
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            String accentedVowel = e.getActionCommand();
            String word = this.addPanel.getForeignTextField().getText();
            int length = this.addPanel.getForeignTextField().getText().length();
            int caretPosition = this.addPanel.getCurrentCaretPosition();
            
            // letters before accented vowel + accented vowel + letters after accenter vowel
            String newWord = word.substring(0, caretPosition) + accentedVowel + 
                             word.substring(caretPosition,length);

            this.addPanel.getForeignTextField().setText(newWord); 
            this.addPanel.getForeignTextField().requestFocusInWindow();  
            this.addPanel.getForeignTextField().setCaretPosition(caretPosition + 1); //set caret after vowel insert
        }
    }
}
