/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.view;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.*;
import vocab.model.log.LogLevel;

/**
 *
 * @author Aaron
 */
public class LogFrameView extends javax.swing.JFrame
{
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
    public LogFrameView()
    {   
        initComponents();  

        this.logTextPane.getCaret().setVisible(true);
        this.setSize(1200, 700);
    }

    /**
     * Sets the log messages map and fills the document with the log messages map.
     * @param messageLog the message logs
     * @throws IOException
     * @throws BadLocationException
     */
    public void init(final LinkedHashMap<String, LogLevel> messageLog) throws IOException, BadLocationException
    {
        this.setLogMessages(messageLog);
        this.fillLog(messageLog);
    }

    /**
     * Sets the window listener for this JFrame.
     * @param listener the listener
     */
    public void addWindowAdapter(final WindowAdapter listener)
    {
        this.addWindowListener(listener);
    }
    
    /**
     * Sets the action listener of search text field.
     * @param listener the listener
     */
    public void addSearchTextFieldListener(final ActionListener listener)
    {
        this.searchTextField.addActionListener(listener);
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
     * Fills the text pane with message logs retrieved from the log file.
     * @param messageLog the message logs
     * @throws BadLocationException
     */
    public void fillLog(final LinkedHashMap<String, LogLevel> messageLog) throws BadLocationException
    {
        this.initDocumentStyle();
        this.document.remove(0, this.document.getLength()); // clears the document

        for(String message: messageLog.keySet())
        {
            switch(this.logMessages.get(message))
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

            this.document.insertString(this.document.getLength(), message + "\n", style);
        }  

        this.logTextPane.setStyledDocument(this.document);
        this.logTextPane.repaint();
    }

    /**
     * @return the logMessages
     */
    public LinkedHashMap<String, LogLevel> getLogMessages() 
    {
        return this.logMessages;
    }

    /**
     * Sets the logMessages.
     * @param logMessages the log messages map to set
     */
    public void setLogMessages(final LinkedHashMap<String, LogLevel> logMessages) 
    {
        this.logMessages = logMessages;
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
    public JTextPane getLogTextPane()
    {
        return logTextPane;
    }

    /**
     * @param logTextPane the logTextPane to set
     */
    public void setLogTextPane(JTextPane logTextPane)
    {
        this.logTextPane = logTextPane;
    }

    /**
     * @return the searchTextField
     */
    public JTextField getSearchTextField() 
    {
        return searchTextField;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Message Log");
        setResizable(false);
        getContentPane().setLayout(null);

        logTextPane.setEditable(false);
        logScrollPane.setViewportView(logTextPane);

        getContentPane().add(logScrollPane);
        logScrollPane.setBounds(20, 10, 1150, 580);

        searchTextField.setFont(ViewManager.VERDANA16);
        getContentPane().add(searchTextField);
        searchTextField.setBounds(20, 610, 1150, 40);
        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextPane logTextPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
