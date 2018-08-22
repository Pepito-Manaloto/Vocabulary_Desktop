/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aaron.desktop.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.aaron.desktop.entity.Vocabulary;
import com.aaron.desktop.model.db.VocabularyRecord;
import com.aaron.desktop.model.others.VocabularyTableCell;
import com.aaron.desktop.view.MainFrameView;
import com.aaron.desktop.view.ViewPanelView;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aaron
 */
public class ViewPanelController
{
    private final ViewPanelView view;
    private final VocabularyRecord model;
    
    public ViewPanelController(final ViewPanelView view, final VocabularyRecord model)
    {
        this.view = view;
        this.model = model;
    }
    
        /**
     * Adds event listener to all components under this panel.
     */
    public void addListeners() 
    {
        ActionListener comboBoxListener = e -> view.refreshTable(model.getVocabularies(view.getLetterComboBoxItem()));
        this.view.addLetterComboBoxListener(comboBoxListener);
        this.view.addVocabularyTableListener(new TableChangeListener(this.view, this.model), new TableKeyListener(this.view, this.model));
    }
    
    /**
     * Inner class for table model listener.
     */
    private static class TableChangeListener implements TableModelListener
    {
        private final ViewPanelView view;
        private final VocabularyRecord model;

        public TableChangeListener(final ViewPanelView view, final VocabularyRecord model)
        {
            this.view = view;
            this.model = model;
        }
        
        /**
         * Fires after a table cell is edited with changes or without changes.
         * Updates the data in database and updates the vocabulary list; upon update.
         * @param e table model event
         */
        @Override
        public void tableChanged(final TableModelEvent e)
        {
            boolean isCellUpdated = e.getLastRow() >= 0 && e.getType() == 0;
            if(isCellUpdated)
            {
                VocabularyTableCell vocabJTable = getVocabularyTableCellFromJTable(e.getLastRow(), view.getDefaultTableModel());
                
                String wordEnglish = view.getDefaultTableModel().getValueAt(e.getLastRow(), 0).toString();
                String wordForeign = view.getDefaultTableModel().getValueAt(e.getLastRow(), 1).toString();

                int response = JOptionPane.showConfirmDialog(this.view, "Do you want to save?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if(response == JOptionPane.YES_OPTION)
                {
                    this.model.updateVocabulary(vocabJTable.getId(), wordEnglish, wordForeign);
                    this.view.refreshTable(this.model.getVocabularies(this.view.getLetterComboBoxItem()));
                }
            }
        }
        
        private VocabularyTableCell getVocabularyTableCellFromJTable(int lastRow, DefaultTableModel table)
        {
            Object column1 = view.getDefaultTableModel().getValueAt(lastRow, 0);
            Object column2 = view.getDefaultTableModel().getValueAt(lastRow, 1);

            if(column1 instanceof VocabularyTableCell)
            {
                return (VocabularyTableCell) column1;
            }
            else if(column2 instanceof VocabularyTableCell)
            {
                return (VocabularyTableCell) column2;
            }
            else
            {
                JOptionPane.showMessageDialog(view, "Failed getting vocabulary info from cell to update", "Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("Expecting one of the cell to be VocabularyTableCell type.");
            }
        }
    }

    /**
     * Inner class for table key listener.
     */
    private static class TableKeyListener extends KeyAdapter
    {
        private final ViewPanelView view;
        private final VocabularyRecord model;

        public TableKeyListener(final ViewPanelView view, final VocabularyRecord model)
        {
            this.view = view;
            this.model = model;
        }

        /**
         * Fires when a table row is selected and delete key is pressed.
         * Deletes the data in the database, removes the data in the JTable row, and updates the vocabulary list; upon delete.
         * @param e key event
         */
        @Override
        public void keyPressed(final KeyEvent e) 
        {
            boolean isDelete = view.getVocabularyTable().getSelectedRow() >= 0 && e.getKeyCode() == KeyEvent.VK_DELETE;
            if (isDelete) 
            {
                String selectedWord = this.view.getDefaultTableModel().getValueAt(this.view.getVocabularyTable().getSelectedRow(), 0).toString();

                int response = JOptionPane.showConfirmDialog(this.view, "Are you sure to delete \"" + selectedWord + "\" from the vocabulary?",
                                                              "Confirmation", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION)
                {
                    this.model.deleteVocabulary(selectedWord);
                    this.view.getDefaultTableModel().removeRow(this.view.getVocabularyTable().getSelectedRow());
                }
            }
        }
    }
}
