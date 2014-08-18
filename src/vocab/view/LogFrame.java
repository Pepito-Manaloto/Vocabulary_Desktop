/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.swing.text.*;
import vocab.model.log.LogManager;
import vocab.model.log.LogLevel;
/**
 *
 * @author Aaron
 */
public class LogFrame extends javax.swing.JFrame
{
    private final LogManager logger = LogManager.getInstance();
    private LinkedHashMap<String, LogLevel> logMessages;
    private final StyleContext context = new StyleContext();
    private final StyledDocument document = new DefaultStyledDocument(context);
    private final Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        
    private static final Color FOREST_GREEN = new Color(34, 139, 34);
    private static final Color ROYAL_BLUE = new Color(65, 105, 225); 
    private static final Color DARK_ORANGE = new Color(255,140,0 );
    private static final Color CRIMSON = new Color(220, 20, 60);
    private static final Color DARK_RED = new Color(139, 0, 0);
    /**
     * Creates new form LogFrame.
     */
    public LogFrame()
    {   
        initComponents();  
        
        if(false == this.init())
        {
            this.dispose();
        }
        this.logTextPane.getCaret().setVisible(true);
        this.addListeners();
        this.setSize(1200, 720);
    }
    
    private void addListeners()
    {
        this.addWindowListener(new WindowAdapter()
            {    
                @Override
                public void windowClosing(WindowEvent e)
                {
                    LogFrame.this.logMessages = null;               
                    LogFrame.this.dispose();
                }
            }); 
        
        this.clearLogMenuItem.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //TODO
                }
            }); 
        this.wordWrapCheckBoxMenuItem.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                   //TODO
                }
            }); 
        this.getSearchTextField().addActionListener(new SearchTextFieldListener(this));
    }
    
    /**
     * Sets up initial style of text pane.
     */
    private void initDocumentStyle()
    {
        StyleConstants.setFontFamily(this.style, "Verdana");
        StyleConstants.setFontSize(this.style, 12);
        StyleConstants.setSpaceAbove(this.style, 1);
        StyleConstants.setSpaceBelow(this.style, 3);
        StyleConstants.setLeftIndent(this.style, 2);
    }
    
    /**
     * Gets all messages from log and fill the text pane.
     * @return true if successful, otherwise false
     */
    private boolean init()
    {
        boolean result;
        
        try 
        {
            this.logMessages = this.getLogger().getMessageLogFromFile("logs/Vocabulary.log");
                 
            this.fillLog(this.getLogMessages());
            result = true;
        } 
        catch (final IOException | BadLocationException ex) 
        {
            result = false;
            this.getLogger().error(ex.toString());
        }
        return result;
    }

    /**
     * Fills the text pane with message logs retrieved from the log file.
     */
    private void fillLog(final LinkedHashMap<String, LogLevel> messageLog) throws BadLocationException
    {
        this.initDocumentStyle();
        
        for(String message: messageLog.keySet())
        {
            switch(this.getLogMessages().get(message))
            {
                case DEBUG: 
                    StyleConstants.setForeground(style, FOREST_GREEN);
                    break;
                case INFO:
                    StyleConstants.setForeground(style, ROYAL_BLUE);
                    break;
                case WARN:
                    StyleConstants.setForeground(style, DARK_ORANGE);
                    break;
                case ERROR:
                    StyleConstants.setForeground(style, CRIMSON);
                    break;    
                case FATAL:
                    StyleConstants.setForeground(style, DARK_RED);
            }

            this.getDocument().insertString(this.getDocument().getLength(), message + "\n", style);
        }  

        this.getLogTextPane().setStyledDocument(this.getDocument());
    }

    /**
     * @return the logMessages
     */
    public LinkedHashMap<String, LogLevel> getLogMessages() 
    {
        return this.logMessages;
    }

    /**
     * @return the document
     */
    public StyledDocument getDocument() 
    {
        return document;
    }

    /**
     * @return the logTextPane
     */
    public javax.swing.JTextPane getLogTextPane()
    {
        return logTextPane;
    }

    /**
     * @param logTextPane the logTextPane to set
     */
    public void setLogTextPane(javax.swing.JTextPane logTextPane)
    {
        this.logTextPane = logTextPane;
    }

    /**
     * @return the searchTextField
     */
    public javax.swing.JTextField getSearchTextField() 
    {
        return searchTextField;
    }

    /**
     * @return the logger
     */
    public LogManager getLogger()
    {
        return logger;
    }

    /**
     * Nested class acts as event listeners for the search text field.
     */
    private static class SearchTextFieldListener implements ActionListener
    {
        private final LogFrame logFrame;
        
        /**
         * Default constructor.
         * @param logFrame the Class that uses this event listeners class
         */
        public SearchTextFieldListener(final LogFrame logFrame)
        {
            this.logFrame = logFrame;
        }
        
        /**
         * action executed by pressing ENTER button. Search through the log messages and returns the matching message with the searched key.
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            final String searchString = this.logFrame.getSearchTextField().getText();
            
            if(searchString.trim().length() > 0)
            {
                try 
                {
                    final LinkedHashMap<String, LogLevel> filteredLogMessages = new LinkedHashMap<>();

                    for(String message: this.logFrame.getLogMessages().keySet())
                    {
                        if(message.contains(searchString))
                        {
                            filteredLogMessages.put(message, this.logFrame.getLogMessages().get(message));
                        }
                    }
                    this.logFrame.getDocument().remove(0, this.logFrame.getDocument().getLength());
                    this.logFrame.fillLog(filteredLogMessages);
                }
                catch(final BadLocationException ex) 
                {
                    this.logFrame.getLogger().error(ex.toString());
                }
            }
        }   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logScrollPane = new javax.swing.JScrollPane();
        logTextPane = new javax.swing.JTextPane();
        searchTextField = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        clearLogMenuItem = new javax.swing.JMenuItem();
        wordWrapCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Message Log");
        setResizable(false);
        getContentPane().setLayout(null);

        logTextPane.setEditable(false);
        logScrollPane.setViewportView(logTextPane);

        getContentPane().add(logScrollPane);
        logScrollPane.setBounds(20, 20, 1150, 560);

        searchTextField.setFont(ViewManager.VERDANA16);
        getContentPane().add(searchTextField);
        searchTextField.setBounds(20, 610, 1150, 40);

        fileMenu.setText("File");

        clearLogMenuItem.setText("Clear log");
        fileMenu.add(clearLogMenuItem);

        wordWrapCheckBoxMenuItem.setSelected(true);
        wordWrapCheckBoxMenuItem.setText("Word Wrap");
        fileMenu.add(wordWrapCheckBoxMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clearLogMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextPane logTextPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JCheckBoxMenuItem wordWrapCheckBoxMenuItem;
    // End of variables declaration//GEN-END:variables
}
