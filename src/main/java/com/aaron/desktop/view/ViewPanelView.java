/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewPanelView.java
 *
 * Created on 12 21, 11, 9:07:18 PM
 */
package com.aaron.desktop.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import com.aaron.desktop.model.db.Vocabulary;

import static com.aaron.desktop.model.db.ForeignLanguage.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * View panel of Vocabulary
 *
 * @author Aaron
 */
public final class ViewPanelView extends javax.swing.JPanel {

    public static final char LETTERS[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                                          'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                          'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final DefaultTableModel table;

    /**
     * Initializes the table and combo box of the panel.
     */
    public ViewPanelView() 
    {
        this.initComponents();

        this.vocabularyTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.table = (DefaultTableModel) this.vocabularyTable.getModel();
    
        this.setSize(650, 490);

        this.letterComboBox.addItem("All");
        this.letterComboBox.addItem("Rec");

        for(char c : LETTERS)
        {
            this.letterComboBox.addItem(String.valueOf(c));
        }
    }
    
   /**
     * Sets the action listener for the sort combo box.
     * @param listener the listener
     */
    public void addLetterComboBoxListener(final ActionListener listener)
    {
        this.letterComboBox.addActionListener(listener);
    }
    
    /**
     * Sets the action listener for the vocabulary JTable default model.
     * @param listener1 the model listener
     * @param listener2 the key listener
     */
    public void addVocabularyTableListener(final TableModelListener listener1, final KeyAdapter listener2)
    {
        this.vocabularyTable.getModel().addTableModelListener(listener1);
        this.vocabularyTable.addKeyListener(listener2);
    }

    /**
     * Returns the selected item in the letter combo box
     * @return a String
     */
    public String getLetterComboBoxItem()
    {
        return this.letterComboBox.getSelectedItem().toString();
    }
    
    /**
     * Returns the vocabulary table.
     * @return a JTable
     */
    public JTable getVocabularyTable()
    {
        return this.vocabularyTable;
    }

    /**
     * Returns the Default model of vocabulary table.
     * @return a DefaultTableModel
     */
    public DefaultTableModel getDefaultTableModel()
    {
        return this.table;
    }
   
    /**
     * Changes the foreign language column with the selected foreign language.
     * @param language selected language
     */
    public void changeSecondColumnHeaderName(final String language)
    {
        this.vocabularyTable.getColumnModel().getColumn(1).setHeaderValue(language);
        this.vocabularyTable.getTableHeader().repaint();
    }

    /**
     * Clears and fills up the vocabulary table
     * @param vocabularyList the vocabulary list
     */
    public void refreshTable(final List<Vocabulary> vocabularyList) 
    {
        this.table.getDataVector().removeAllElements(); // Clears the table

        Function<Vocabulary, Object[]> vocabularyToObjectArray = (v) -> v.getVocabularyAsObject();
        Consumer<Object[]> addVocabularyToTable = (v) -> this.table.addRow(v);
        vocabularyList.stream().map(vocabularyToObjectArray).forEach(addVocabularyToTable);

        if(Hokkien.equals(MainFrameView.getforeignLanguage()))
        {
            setVocabularyTablePropertiesOnEnglishCharacters();
        }
        else
        {
            setVocabularyTablePropertiesOnNonEnglishCharacters();
        }

        this.vocabularyTable.repaint();
    }
    
    private void setVocabularyTablePropertiesOnEnglishCharacters()
    {
        this.vocabularyTable.setFont(ViewManager.MEIRYO14);
        this.vocabularyTable.setRowHeight(16);
        this.vocabularyTable.getColumnModel().getColumn(0).setPreferredWidth(80);
    }
    
    private void setVocabularyTablePropertiesOnNonEnglishCharacters()
    {
        this.vocabularyTable.setFont(ViewManager.MEIRYO19);
        this.vocabularyTable.setRowHeight(26);
        this.vocabularyTable.getColumnModel().getColumn(0).setPreferredWidth(300);
    }

    /**
     * Initializes all GUI. Generated by NetBeans.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScrollPane = new javax.swing.JScrollPane();
        vocabularyTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        letterComboBox = new javax.swing.JComboBox<>();

        setOpaque(false);
        setLayout(null);

        vocabularyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "English", "Hokkien"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        vocabularyTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        vocabularyTable.getTableHeader().setReorderingAllowed(false);
        tableScrollPane.setViewportView(vocabularyTable);
        vocabularyTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        vocabularyTable.setColumnSelectionAllowed(false);
        vocabularyTable.setRowSelectionAllowed(true);

        add(tableScrollPane);
        tableScrollPane.setBounds(20, 20, 520, 480);

        jLabel1.setText("Sort:");
        add(jLabel1);
        jLabel1.setBounds(550, 20, 39, 14);

        jLabel2.setText("Letter:   ");
        add(jLabel2);
        jLabel2.setBounds(550, 50, 60, 20);

        letterComboBox.setMaximumRowCount(27);
        add(letterComboBox);
        letterComboBox.setBounds(610, 50, 50, 20);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox<String> letterComboBox;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable vocabularyTable;
    // End of variables declaration//GEN-END:variables
}
