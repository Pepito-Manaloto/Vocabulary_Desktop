/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vocab.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import vocab.model.db.Vocabulary;
import vocab.model.db.VocabularyRecord;
import vocab.view.MainFrameView;
import vocab.view.ViewPanelView;

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
        this.view.addLetterComboBoxListener(new ActionListener()
            {
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    view.refreshTable(model.getVocabularies(view.getLetterComboBoxItem()));
                }
            });

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
            if (e.getLastRow() >= 0 && e.getType() == 0) // Cell Updated
            {
                String wordEnglish = this.view.getDefaultTableModel().getValueAt(e.getLastRow(), 0).toString();
                String wordForeign = this.view.getDefaultTableModel().getValueAt(e.getLastRow(), 1).toString();

                int response = JOptionPane.showConfirmDialog(this.view, "Do you want to save?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION)
                {
                    this.model.updateVocabulary(new Vocabulary(wordEnglish, wordForeign,
                                                               MainFrameView.getforeignLanguage()),
                                                               e.getColumn());
                }
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
            if (this.delete(e)) 
            {
                String selectedVocabulary = this.view.getDefaultTableModel().getValueAt(this.view.getVocabularyTable().getSelectedRow(), 0).toString();

                int response = JOptionPane.showConfirmDialog(this.view, "Are you sure to delete \"" + selectedVocabulary + "\" from the vocabulary?",
                                                              "Confirmation", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION)
                {
                    this.model.deleteVocabulary(selectedVocabulary);
                    this.view.getDefaultTableModel().removeRow(this.view.getVocabularyTable().getSelectedRow());
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
            return this.view.getVocabularyTable().getSelectedRow() >= 0 && e.getKeyCode() == KeyEvent.VK_DELETE;
        }
    }
}
