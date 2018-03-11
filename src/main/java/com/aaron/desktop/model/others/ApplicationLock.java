/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.others;

import static java.util.Objects.*;

import com.aaron.desktop.model.log.LogManager;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javax.swing.JOptionPane;

/**
 * Ensures only one instance of an application runs.
 * @author Aaron
 */
public final class ApplicationLock
{          
    private final LogManager logger = LogManager.getInstance();
    private final String className = this.getClass().getSimpleName();
    private static final String LOCK_FILE_NAME = "appLock";
    
    private File lockFile = null;
    private FileChannel channel = null;
    private FileLock lock = null;   
    
    

    /**
     * Ensures that only one application can run at a time. Uses FileLock technique.  
     * @param message the file name of the lock
     */
    public void lockApplication(final String message)
    {
        try
        {      
            this.lockFile = new File(LOCK_FILE_NAME);

            this.channel = new RandomAccessFile(this.lockFile, "rw").getChannel(); // get channel from the file
            this.lock = this.channel.tryLock(); // acquire lock from the file

            if(isNull(lock)) // lock is null if it is already acquired before from the file
            {  
                JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        catch(final IOException ex)
        {
            this.logger.error(this.className, "lockApplication(String)", ex.toString(), ex);
        }
    }
    
    /**
     * Releases the resources used by this class. Deletes the file "appLock"
     */
    public void releaseResources()
    {
        try
        {
            if(nonNull(lock) && this.lock.isValid())
            {
                this.lock.release();
            }

            if(nonNull(channel))
            {
                this.channel.close();
            }

            if(nonNull(lockFile) && this.lockFile.exists())
            {
                this.lockFile.delete();
            }
        }
        catch(IOException ex)
        {
            this.logger.error(this.className, "releaseResources()", ex.toString(), ex);
        }
    }
}
