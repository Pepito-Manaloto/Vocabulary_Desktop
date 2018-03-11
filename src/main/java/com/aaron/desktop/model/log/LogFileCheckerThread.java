/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aaron.desktop.model.log;

import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import com.aaron.desktop.view.LogFrameView;

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
            EventQueue.invokeLater(this::updateLogFrame);
            LogManager.finishedUpdating();
        }
    }

    /**
     * Updates the log frame's content.
     */
    private void updateLogFrame()
    {
        try 
        {
            logFrame.init(logger.getMessageLogFromFile());
        } 
        catch (IOException | BadLocationException ex)
        {
            logger.error(this.getClass().getSimpleName(), "updateLogFrame()", ex.toString(), ex);
        }
    }
}
