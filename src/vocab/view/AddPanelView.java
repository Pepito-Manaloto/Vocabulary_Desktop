/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddPanelView.java
 *
 * Created on 12 21, 11, 9:07:12 PM
 */
package vocab.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

/**
 * Add Panel GUI.
 * @author Aaron
 */
public final class AddPanelView extends javax.swing.JPanel 
{
    public static final int NUMBER_OF_ACCENTED_BUTTONS = 24;
    public static final String[] ACCENTED_LETTERS = {"ā","á","â","à","ē","é","ê","è",
                                                     "ī","í","î","ì","ō","ó","ô","ò",
                                                     "ū","ú","û","ù", "ⁿ","ń","ň","ǹ"};
    public static final int[] BUTTON_BOUND_MULTIPLIER = {0, 60, 120, 180};   

    private int currentCaretPosition;
    private final JButton[] accentedButtons;
    
    /**
     * Initialization.
     */
    public AddPanelView()
    {     
        accentedButtons = new JButton[NUMBER_OF_ACCENTED_BUTTONS];

        this.initComponents();
        this.setSize(650, 490); 
        this.initAccentedButtons();
    }

    /**
     * Adds accented buttons into panel.
     */
    private void initAccentedButtons()
    {         
        for(int i = 0; i < NUMBER_OF_ACCENTED_BUTTONS; i++)
        {
            this.accentedButtons[i] = new JButton();  
            this.accentedButtons[i].setText(ACCENTED_LETTERS[i]);   

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
     * Sets the action listener for each accented JButtons.
     * @param listener the listener
     */
    public void addAccentedButtonsListener(final ActionListener listener)
    {
        for(int i = 0; i < NUMBER_OF_ACCENTED_BUTTONS; i++)
        {
            this.accentedButtons[i].addActionListener(listener);  
        }
    }
    
    /**
     * Sets the action listener for add button.
     * @param listener the listener
     */
    public void addAddButtonListener(final ActionListener listener)
    {
        this.addButton.addActionListener(listener);
    }

    /**
     * Sets the key listener for English text field.
     * @param listener the listener
     */
    public void addEnglishTextFieldListener(final KeyListener listener)
    {
        this.englishTextField.addKeyListener(listener);
    }

    /**
     * Sets the key listener for foreign text field.
     * @param listener1 the key listener
     * @param listener2 the caret listener
     */
    public void addForeignTextFieldListener(final KeyListener listener1, final CaretListener listener2)
    {
        this.foreignTextField.addKeyListener(listener1);
        this.foreignTextField.addCaretListener(listener2);
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
    public boolean isTextFieldsEmpty()
    {
        return (this.englishTextField.getText().length() <= 0 || this.foreignTextField.getText().length() <= 0);
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
     * Sets the current caret position.
     * @param currentCaretPosition the caret position to set to
     */
    public void setCurrentCaretPosition(final int currentCaretPosition) 
    {
        this.currentCaretPosition = currentCaretPosition;
    }
    
    /**
     * @return the currentCaretPosition
     */
    public int getCurrentCaretPosition() 
    {
        return this.currentCaretPosition;
    }

    /**
     * @return the foreignTextField
     */
    public JTextField getForeignTextField() 
    {
        return this.foreignTextField;
    }
    
    /**
     * @return the englishTextField
     */
    public JTextField getEnglishTextField() 
    {
        return this.englishTextField;
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
