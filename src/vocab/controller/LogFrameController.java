/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vocab.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.text.BadLocationException;
import vocab.model.log.LogFileCheckerThread;
import vocab.model.log.LogLevel;
import vocab.model.log.LogManager;
import vocab.view.LogFrameView;

/**
 *
 * @author Aaron
 */
public class LogFrameController
{
    private final LogManager logger;
    private LogFrameView view;

    public LogFrameController(final LogFrameView logFrame, final LogManager logger) 
    {
        this.view = logFrame;
        this.logger = logger;
    }
    
    /**
     * Adds event listener to all components under this controller's view.
     * And initializes scheduled log file update checker thread.
     */
    public void addListeners()
    {        
        // Runs every 0.5 second after 2 seconds after the log frame is opened.
        final LogFileCheckerThread logFileCheckerThread = new LogFileCheckerThread(this.view);
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        final ScheduledFuture<?> serviceScheduler = service.scheduleWithFixedDelay(logFileCheckerThread,
                                                                                   2000,
                                                                                   500,
                                                                                   TimeUnit.MILLISECONDS);

        this.view.addWindowAdapter(new WindowAdapter()
            {    
                @Override
                public void windowClosing(WindowEvent e)
                {
                    view.setLogMessages(null);
                    view.dispose();

                    // Stops and shutdown log file update checker thread.
                    serviceScheduler.cancel(true);
                    service.shutdown();
                }
            }); 

        this.view.addSearchTextFieldListener(new SearchTextFieldListener(this.view, this.logger));
        
        try 
        {
            this.view.init(this.logger.getMessageLogFromFile("logs/vocabulary.log"));
        } 
        catch(final IOException | BadLocationException ex) 
        {
            this.logger.error(ex.toString(), ex);
        }
    }
    
    /**
     * Nested class acts as event listeners for the search text field.
     */
    private static class SearchTextFieldListener implements ActionListener
    {
        private final LogFrameView logFrame;
        private final LogManager logger;

        /**
         * Default constructor.
         * @param logFrame the Class that uses this event listeners class
         */
        public SearchTextFieldListener(final LogFrameView logFrame, final LogManager logger)
        {
            this.logFrame = logFrame;
            this.logger = logger;
        }
        
        /**
         * Action executed by pressing ENTER button. Search through the log messages and returns the matching message with the searched key.
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String searchString = this.logFrame.getSearchTextField().getText();
            
            if(searchString.trim().length() > 0)
            {
                try 
                {
                    LinkedHashMap<String, LogLevel> filteredLogMessages = new LinkedHashMap<>();

                    for(String message: this.logFrame.getLogMessages().keySet())
                    {
                        if(message.contains(searchString))
                        {
                            filteredLogMessages.put(message, this.logFrame.getLogMessages().get(message));
                        }
                    }

                    this.logFrame.fillLog(filteredLogMessages);
                }
                catch(final BadLocationException ex) 
                {
                    this.logger.error(ex.toString(), ex);
                }
            }
            else
            {
                try 
                {
                    this.logFrame.init(this.logger.getMessageLogFromFile("logs/vocabulary.log"));
                } 
                catch(final IOException | BadLocationException ex) 
                {
                    this.logger.error(ex.toString(), ex);
                }
            }
        }   
    }
    
}
