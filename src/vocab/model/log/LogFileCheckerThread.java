/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vocab.model.log;

import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import vocab.view.LogFrameView;

/**
 *
 * @author Aaron
 */
public class LogFileCheckerThread implements Runnable
{
    private static final LogManager logger = LogManager.getInstance();
    private final LogFrameView logFrame;
    
    public LogFileCheckerThread(final LogFrameView logFrame)
    {
        this.logFrame = logFrame;
    }
    
    /**
     * Runs a watch service for any log file changes in a separate thread.
     */
    @Override
    public void run() 
    {
        if(LogManager.isUpdated())
        {
            this.updateLogFrame();
            LogManager.finishedUpdating();
        }
    }
    
    /**
     * Updates the log frame's content.
     */
    private void updateLogFrame()
    {
        EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    try 
                    {
                        logFrame.init(logger.getMessageLogFromFile("logs/vocabulary.log"));
                    } 
                    catch (final IOException | BadLocationException ex)
                    {
                        logger.error(ex.toString(), ex);
                    }
                }
            });
    }
}