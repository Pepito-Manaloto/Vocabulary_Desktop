/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddPanel.java
 *
 * Created on 12 21, 11, 9:07:12 PM
 */
package main.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import main.model.Vocabulary;
import main.model.VocabularyRecord;

/**
 * Add Panel GUI.
 * @author Aaron
 */
public final class AddPanel extends javax.swing.JPanel 
{
    public static final int NUMBER_OF_ACCENTED_BUTTONS = 24;
    public static final String[] ACCENTED_LETTERS = {"ā","á","â","à","ē","é","ê","è",
                                                     "ī","í","î","ì","ō","ó","ô","ò",
                                                     "ū","ú","û","ù", "ⁿ","ń","ň","ǹ"};
    public static final int[] BUTTON_BOUND_MULTIPLIER = {0, 60, 120, 180};   
    
    private VocabularyRecord vRecord = null;
    private int currentCaretPosition = 0;
    private final JButton[] accentedButtons = new JButton[NUMBER_OF_ACCENTED_BUTTONS];
    
    /**
     * Initialization.
     * @param vRecord the class used for transaction with the database
     */
    public AddPanel(final VocabularyRecord vRecord)
    {               
        this.initComponents();
        this.setSize(650, 490); 
        this.vRecord = vRecord;   
        this.initAccentedButtons();
        this.addListeners();
    }

    /**
     * Adds event listener to all components under this panel.
     */
    private void addListeners()
    {
        this.getForeignTextField().addCaretListener(
            new CaretListener() 
            {
                @Override
                public void caretUpdate(final CaretEvent ce)
                {
                    currentCaretPosition = ce.getDot();
                }
            }); 

        this.addButton.addActionListener(new AddButtonListener(this));
        this.englishTextField.addKeyListener(new FieldAddButtonListener(this));
        this.foreignTextField.addKeyListener(new FieldAddButtonListener(this));
    }
    
    /**
     * Adds accented buttons into panel.
     */
    private void initAccentedButtons()
    {         
        int i;
        for(i = 0; i < NUMBER_OF_ACCENTED_BUTTONS; i++)
        {
            this.accentedButtons[i] = new JButton();  
            this.accentedButtons[i].setText(ACCENTED_LETTERS[i]);   
            
            this.accentedButtons[i].addActionListener(new AccentedButtonListener(this));  
            
            if(i < 4)
            {
                this.accentedButtons[i].setBounds(50 + BUTTON_BOUND_MULTIPLIER[i], 130, 50, 40);
            }
            else if(i >= 4 && i < 8)
            {
                this.accentedButtons[i].setBounds(50 + BUTTON_BOUND_MULTIPLIER[i - 4], 190, 50, 40);
            }
            else if(i >= 8 && i < 12)
            {
                this.accentedButtons[i].setBounds(360 + BUTTON_BOUND_MULTIPLIER[i - 8], 130, 50, 40);
            }
            else if(i >= 12 && i < 16)
            {
                this.accentedButtons[i].setBounds(360 + BUTTON_BOUND_MULTIPLIER[i - 12], 190, 50, 40);
            }
            else if(i >= 16 && i < 20)
            {
                this.accentedButtons[i].setBounds(50  + BUTTON_BOUND_MULTIPLIER[i - 16], 248, 50, 40);
            }
            else if(i >= 20 && i < 24)
            {
                this.accentedButtons[i].setBounds(360  + BUTTON_BOUND_MULTIPLIER[i - 20], 248, 50, 40);
            }

            this.add(this.accentedButtons[i]);
        }
        // Change the font of ǹ, to be displayed.
        this.accentedButtons[NUMBER_OF_ACCENTED_BUTTONS - 1].setFont(ViewManager.MEIRYO14);
    }
    
    /**
     * Clears both text fields.
     */
    public void clearTextfields()
    {
        this.englishTextField.setText("");
        this.foreignTextField.setText("");
    }
 
    /**
     * Checks if both text fields are empty.
     * @return true if empty, otherwise false
     */
    private boolean isTextFieldsEmpty()
    {
        return (this.englishTextField.getText().length() <= 0 || this.foreignTextField.getText().length() <= 0);
    }
    
    /**
     * Adds the data to the database.
     */
    private void insertToDatabase()
    {
        if(this.isTextFieldsEmpty())
        {
            JOptionPane.showMessageDialog(this, "Complete all fields.", "Note", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            final boolean success = this.vRecord.addToDatabase(new Vocabulary(this.englishTextField.getText().trim(), 
                                                                              this.foreignTextField.getText().trim(), 
                                                                              MainFrame.getforeignLanguage()));

            if(success)
            {
                this.clearTextfields();
                this.englishTextField.requestFocusInWindow();
                JOptionPane.showMessageDialog(this, "Added.", "Note", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Changes the language label with the selected language.
     * @param language selected language
     */
    public void setLanguageLabel(final String language)
    {
        this.foreignLanguageLabel.setText(language + ":"); 
    }

    /**
     * @return the currentCaretPosition
     */
    public int getCurrentCaretPosition() 
    {
        return currentCaretPosition;
    }

    /**
     * @return the foreignTextField
     */
    public JTextField getForeignTextField() 
    {
        return foreignTextField;
    }

    /**
     Inner class for accented button action.
     */
    private static class AccentedButtonListener implements ActionListener
    {
        private final AddPanel addPanel;
        
        public AccentedButtonListener(final AddPanel addPanel)
        {
            this.addPanel = addPanel;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            final String accentedVowel = e.getActionCommand();
            final String word = this.addPanel.getForeignTextField().getText();
            final int length = this.addPanel.getForeignTextField().getText().length();
            final int caretPosition = this.addPanel.getCurrentCaretPosition();
            
            // letters before accented vowel + accented vowel + letters after accenter vowel
            final String newWord = word.substring(0, caretPosition) + accentedVowel + 
                             word.substring(caretPosition,length);

            this.addPanel.getForeignTextField().setText(newWord); 
            this.addPanel.getForeignTextField().requestFocusInWindow();  
            this.addPanel.getForeignTextField().setCaretPosition(caretPosition + 1); //set caret after vowel insert
        }
    }
    
    /**
     Inner class for add button action.
     */
    private static class AddButtonListener implements ActionListener
    { 
        private final AddPanel addPanel;
        
        public AddButtonListener(final AddPanel addPanel)
        {
            this.addPanel = addPanel;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            this.addPanel.insertToDatabase();
        }
    }
    
    /**
     Inner class for add button action.
     */
    private static class FieldAddButtonListener extends KeyAdapter
    { 
        private final AddPanel addPanel;
        
        public FieldAddButtonListener(final AddPanel addPanel)
        {
            this.addPanel = addPanel;
        }

        @Override
        public void keyPressed(final KeyEvent e) 
        {
            if(KeyEvent.VK_ENTER == e.getKeyCode())
            {
                this.addPanel.insertToDatabase();
            }
        }
    }
     /**
     Initializes all GUI. Generated by NetBeans. 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        foreignLanguageLabel = new javax.swing.JLabel();
        englishTextField = new javax.swing.JTextField();
        foreignTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();

        setOpaque(false);
        setLayout(null);

        jLabel1.setText("English:");
        add(jLabel1);
        jLabel1.setBounds(21, 29, 61, 20);

        foreignLanguageLabel.setText("Hokkien: ");
        add(foreignLanguageLabel);
        foreignLanguageLabel.setBounds(320, 30, 80, 20);
        add(englishTextField);
        englishTextField.setBounds(80, 30, 208, 20);
        add(foreignTextField);
        foreignTextField.setBounds(390, 30, 222, 20);

        jLabel4.setText("Accented vowels:");
        add(jLabel4);
        jLabel4.setBounds(20, 90, 115, 14);

        addButton.setText("Add");
        add(addButton);
        addButton.setBounds(260, 330, 120, 50);
    }// </editor-fold>//GEN-END:initComponents

      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTextField englishTextField;
    private javax.swing.JLabel foreignLanguageLabel;
    private javax.swing.JTextField foreignTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
