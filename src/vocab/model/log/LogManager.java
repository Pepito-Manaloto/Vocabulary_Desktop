/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Aaron
 */
public final class LogManager
{    
    private final Logger logger;
    private final String className;
    private static final LogManager logManager = new LogManager();
    private final static AtomicBoolean updated = new AtomicBoolean(); // In-case multiple threads use this flag.
    
    /**
     * Private constructor initializes log4j. 
     */
    private LogManager()
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        
        // Get the last class that calls LogManager
        this.className = stacktrace[stacktrace.length - 1].getClassName();
        this.logger = org.apache.logging.log4j.LogManager.getLogger();
    }
    
    /**
     * Gets the value of the log file update flag
     * @return boolean true if there are updates, else false
     */
    public static boolean isUpdated()
    {
        return updated.get();
    }
    
    /**
     * Sets the log file update flag to false.
     */
    public static void finishedUpdating()
    {
        updated.set(false);
    }
    
    /**
     * Gets a LogManager instance, ensures singleton.
     * @return LogManager instance
     */
    public static LogManager getInstance()
    {
        return logManager;
    }
    
    /**
     * Logs in debug level.
     * @param message the log message
     */
    public void debug(final String message)
    {
       this.logger.debug(this.className + " in " + this.getMethodName() + ": " + message);
       updated.set(true);
    }
    
    /**
     * Logs in info level.
     * @param message the log message
     */
    public void info(final String message)
    {
        this.logger.info(this.className + " in " + this.getMethodName() + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in warn level.
     * @param message the log message
     */
    public void warn(final String message)
    {
        this.logger.warn(this.className + " in " + this.getMethodName() + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in error level.
     * @param message the log message
     */
    public void error(final String message)
    {
        this.logger.error(this.className + " in " + this.getMethodName() + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in error level.
     * @param message the log message
     * @param e exception stack trace
     */
    public void error(final String message, final Throwable e)
    {
        this.logger.error(this.className + " in " + this.getMethodName() + ": " + message, e);
        updated.set(true);
    }

    /**
     * Logs in fatal level.
     * @param message the log message
     */
    public void fatal(final String message)
    {
        this.logger.fatal(this.className + " in " + this.getMethodName() + ": " + message);
        updated.set(true);
    }

    /**
     * Logs in fatal level.
     * @param message the log message
     * @param e exception stack trace
     */
    public void fatal(final String message, final Throwable e)
    {
        this.logger.fatal(this.className + " in " + this.getMethodName() + ": " + message, e);
        updated.set(true);
    }

    /**
     * Gets all messages in the log and stores it to a map with the message as key.
     * @param logPath the path of the log file
     * @throws IOException
     * @return the log messages
     */
    public LinkedHashMap<String, LogLevel> getMessageLogFromFile(final String logPath) throws IOException
    {
        LinkedHashMap<String, LogLevel> messageList = new LinkedHashMap<>();
        
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        String line;
        
        while( (line = br.readLine()) != null) //Gets each message
        {
            for(LogLevel level: LogLevel.values()) //Puts message as the key and it log level as value
            {
                if(line.contains(level.toString()))
                {
                    messageList.put(line, level);
                }     
            }    
        }
        
        return messageList;
    }

    /**
     * Gets the method name of the current executing method.
     * @return the method name
     */
    private String getMethodName()
    {
        final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        
        //Get the first method of the class calling getMethodName()
        for(StackTraceElement stackElement: stacktrace)
        {
            if(this.className.equals(stackElement.getClassName()))
            {
                return stackElement.getMethodName();
            }
        }
        
        return null;
    }
}