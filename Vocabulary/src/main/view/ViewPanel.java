/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewPanel.java
 *
 * Created on 12 21, 11, 9:07:18 PM
 */
package main.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import main.model.ForeignLanguage;
import main.model.Vocabulary;
import main.model.VocabularyRecord;

/**
 * View panel of Vocabulary
 *
 * @author Aaron
 */
public final class ViewPanel extends javax.swing.JPanel {

    public static final char LETTERS[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private VocabularyRecord vRecord = null;
    private DefaultTableModel table = null;
    private String selectedVocabulary = null;
    private Vocabulary[] vocabularyList;

    /**
     * Initializes the table and combo box of the panel.
     *
     * @param vRecord the class used for transaction with the database
     */
    public ViewPanel(final VocabularyRecord vRecord) 
    {
        this.vocabularyList = null;
        this.vRecord = vRecord;

        this.initComponents();

        this.vocabularyTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.table = (DefaultTableModel) this.vocabularyTable.getModel();
    
        this.setSize(650, 490);

        this.letterComboBox.addItem("All");

        for(char c : LETTERS)
        {
            this.letterComboBox.addItem(String.valueOf(c));
        }

        this.addListeners();
    }

    /**
     * Returns the Vocabulary table.
     *
     * @return a JTable
     */
    public JTable getVocabularyTable()
    {
        return this.vocabularyTable;
    }

    /**
     * Changes the foreign language column with the selected foreign language.
     *
     * @param language selected language
     */
    public void changeSecondColumnHeaderName(final String language)
    {
        this.vocabularyTable.getColumnModel().getColumn(1).setHeaderValue(language);
        this.vocabularyTable.getTableHeader().repaint();
    }

    /**
     * Clear and fill up the vocabulary table
     *
     * @see #clearTable
     * @see #fillTable
     */
    public void refreshTable() 
    {
        this.clearTable();

        this.vocabularyList = this.vRecord.getVocabularies(this.letterComboBox.getSelectedItem().toString());
        this.fillTable(this.vocabularyList);
        
        if(ForeignLanguage.HOKKIEN.toString().equals(MainFrame.getforeignLanguage()))
        {
            this.vocabularyTable.setFont(ViewManager.MEIRYO14);
            this.vocabularyTable.setRowHeight(16);
            this.vocabularyTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        }
        else //Increase size for non english characters
        {
            this.vocabularyTable.setFont(ViewManager.MEIRYO19);
            this.vocabularyTable.setRowHeight(26);
            this.vocabularyTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        }

        this.vocabularyTable.repaint();
    }

    /**
     * Adds event listener to all components under this panel.
     */
    private void addListeners() 
    {
        this.letterComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                refreshTable();
            }
        });

        this.vocabularyTable.getModel().addTableModelListener(new TableChangeListener());
        this.vocabularyTable.addKeyListener(new TableKeyListener());
    }

    /**
     * Add rows in the vocabulary table.
     *
     * @param row 2D array of vocabularies
     */
    private void fillTable(final Vocabulary row[]) 
    {
        for (Vocabulary v : row) 
        {
            this.table.addRow(v.getVocabularyAsObject());
        }
    }

    /**
     * Remove all data in the vocabulary table.
     */
    private void clearTable() 
    {
        this.table.getDataVector().removeAllElements();
    }

    /**
     * Inner class for table model listener.
     */
    private class TableChangeListener implements TableModelListener
    {
        private String wordEnglish = null;
        private String wordForeign = null;
        private int response = 0;

        @Override
        public void tableChanged(final TableModelEvent e)
        {
            if (e.getLastRow() >= 0 && e.getType() == 0) //Cell Updated
            {
                this.wordEnglish = table.getValueAt(e.getLastRow(), 0).toString();
                this.wordForeign = table.getValueAt(e.getLastRow(), 1).toString();

                this.response = JOptionPane.showConfirmDialog(ViewPanel.this, "Do you want to save?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (this.response == JOptionPane.YES_OPTION)
                {
                    vRecord.updateVocabulary(new Vocabulary(this.wordEnglish,
                                                                           this.wordForeign,
                                                                           MainFrame.getforeignLanguage()),
                                                                           e.getColumn());
                }
                else 
                {
                    refreshTable();
                }
            }
        }
    }

    /**
     * Inner class for table key listener.
     */
    private class TableKeyListener extends KeyAdapter
    {
        private int response = 0;

        @Override
        public void keyPressed(final KeyEvent e) 
        {
            if (this.delete(e)) 
            {
                selectedVocabulary = table.getValueAt(vocabularyTable.getSelectedRow(), 0).toString();

                this.response = JOptionPane.showConfirmDialog(ViewPanel.this, "Are you sure to delete \"" + selectedVocabulary + "\" from the vocabulary?",
                                                              "Confirmation", JOptionPane.YES_NO_OPTION);

                if (this.response == JOptionPane.YES_OPTION)
                {
                    vRecord.deleteVocabulary(selectedVocabulary);
                    table.removeRow(vocabularyTable.getSelectedRow());
                }
            }
        }

        /**
         * Checks if there is a row selected and if delete key is pressed.
         *
         * @param e key event
         */
        private boolean delete(final KeyEvent e)
        {
            return vocabularyTable.getSelectedRow() >= 0 && e.getKeyCode() == KeyEvent.VK_DELETE;
        }
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

        vocabularyTable.setAutoCreateRowSorter(true);
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
