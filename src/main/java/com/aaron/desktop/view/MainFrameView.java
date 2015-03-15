/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrameView.java
 *
 * Created on 12 21, 11, 6:26:00 PM
 */
package com.aaron.desktop.view;

import com.aaron.desktop.model.db.ForeignLanguage;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import com.aaron.desktop.model.others.Resource;

import static com.aaron.desktop.model.db.ForeignLanguage.*;
import static com.aaron.desktop.view.MainFrameView.PanelName.*;

/**
 *
 * @author Aaron
 */
public final class MainFrameView extends javax.swing.JFrame
{
    /**
     * Enum constants for card layout panel names.
     */
    public enum PanelName
    {
        Add, View;
    }
    
    private final CardLayout cardLayout;
    private final ViewPanelView view;
    private final AddPanelView add;
    private JTextField searchTextField;
    private static String selectedForeignLanguage = "Hokkien";

    /**
     * Sets up the background image, Layout, and actionListeners.
     * @param add the add panel
     * @param view the view panel
     */        
    public MainFrameView(final AddPanelView add, final ViewPanelView view) 
    {
        this.add = add;
        this.view = view; 
        this.initComponents();
        this.setSize(891,561);

        setForeignLanguage(Hokkien.toString());

        this.cardLayout = new CardLayout();
        this.setIconImage(new ImageIcon(getClass().getResource(Resource.IMAGE_ICON)).getImage());

        this.mainPanel.setLayout(this.cardLayout); 
        this.mainPanel.add(this.add, Add.name());
        this.mainPanel.add(this.view, View.name());
        
        this.searchTextField.setVisible(false);    
        this.suggestListScrollPane.setVisible(false); 

        for(ForeignLanguage language: ForeignLanguage.values())
        {
            this.foreignLanguageComboBox.addItem(language.toString());
        }
    }

    /**
     * Sets the action listener for add JButton.
     * @param listener the listener
     */
    public void addAddButtonListener(final ActionListener listener)
    {
        this.addButton.addActionListener(listener);
    }
    
    /**
     * Sets the action listener for view JButton.
     * @param listener the listener
     */
    public void addViewButtonListener(final ActionListener listener)
    {
        this.viewButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for foreign language combo box.
     * @param listener the listener
     */
    public void addForeignLanguageComboBoxListener(final ActionListener listener)
    {
        this.foreignLanguageComboBox.addActionListener(listener);
    }

    /**
     * Sets the action listener for suggestion JList.
     * @param listener the listener
     */
    public void addSuggestionListListener(final MouseAdapter listener)
    {
        this.suggestionList.addMouseListener(listener);
        this.suggestionList.addMouseMotionListener(listener);
    }
    
    /**
     * Sets the action listener for search text field.
     * @param listener the listener
     */
    public void addSearchTextFieldListener(final EventListener listener)
    {
        this.searchTextField.addKeyListener((KeyListener)listener);
        this.searchTextField.addCaretListener((CaretListener)listener);
        this.searchTextField.addFocusListener((FocusListener)listener);
    }
    
    /**
     * Sets the action listener for backup JButton.
     * @param listener the listener
     */
    public void addBackupButtonListener(final ActionListener listener)
    {
        this.backupButton.addActionListener(listener);
    }
    
    /**
     * Sets the action listener for show logs menu item.
     * @param listener the listener
     */
    public void addShowLogsMenuItemListener(final ActionListener listener)
    {
        this.showLogsMenuItem.addActionListener(listener);
    }

    /**
     * Sets the action listener for about menu item.
     * @param listener the listener
     */
    public void addAboutMenuItemListener(final ActionListener listener)
    {
        this.aboutMenuItem.addActionListener(listener);
    }
    
    /**
     * Sets the card layout panel to the given panel name.
     * @param name the JPanel name
     */
    public void showPanel(final PanelName name)
    {
        this.cardLayout.show(this.mainPanel, name.toString());
    }

    /**
     * Returns AddPanelView.
     * @return AddPanelView
     */
    public AddPanelView getAddPanelView()
    {
        return this.add;
    }

    /**
     * Returns ViewPanelView.
     * @return ViewPanelView
     */
    public ViewPanelView getViewPanelView()
    {
        return this.view;
    }
    
    /**
     * Returns JTextField.
     * @return JTextField
     */
    public JTextField getSearchTextField()
    {
        return this.searchTextField;
    }
    
    /**
     * Returns JScrollPane.
     * @return JScrollPane
     */
    public JScrollPane getSuggestListScrollPane()
    {
        return this.suggestListScrollPane;
    }

    /**
     * Returns selected foreign language.
     * @return String
     */
    public String getForeignLanguageComboBoxItem()
    {
        return this.foreignLanguageComboBox.getSelectedItem().toString();
    }
    
    /**
     * Returns JList.
     * @return JList
     */
    public JList<String> getSuggestionList()
    {
        return this.suggestionList;
    }

    /**
     * Returns the selected language.
     * @return enum ForeignLanguage.
     */
    public static ForeignLanguage getforeignLanguage()
    {
        switch(selectedForeignLanguage)
        {
            case "Hokkien": 
                return Hokkien;
            case "Japanese":
                return Japanese;
            case "Mandarin":
                return Mandarin;
            default:
                throw new AssertionError("Unsupported foreign language.");
        }
    }

    /**
     * Sets the foreign language.
     * @param foreignLanguage the language to set.
     */
    public static void setForeignLanguage(final String foreignLanguage)
    {
        selectedForeignLanguage = foreignLanguage;
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
        aboutMenuItem = new javax.swing.JMenuItem();

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
            URL url = MainFrameView.class.getResource(Resource.IMAGE_SEARCH_ICON);
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

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource(Resource.IMAGE_BACKGROUND)));
        jLayeredPane1.add(background);
        background.setBounds(0, 0, 890, 510);

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(0, 0, 900, 510);

        viewMenu.setText("  View  ");

        showLogsMenuItem.setText("    Show Logs    ");
        showLogsMenuItem.setToolTipText("");
        viewMenu.add(showLogsMenuItem);

        aboutMenuItem.setText("    About");
        viewMenu.add(aboutMenuItem);

        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
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
