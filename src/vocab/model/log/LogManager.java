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
    private static final LogManager logManager = new LogManager();
    private final static AtomicBoolean updated = new AtomicBoolean(); // In-case multiple threads use this flag.

    /**
     * Private constructor initializes log4j. 
     */
    private LogManager()
    {
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
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     */
    public void debug(final String className, final String methodName, final String message)
    {
       this.logger.debug(className + " in " + methodName + ": " + message);
       updated.set(true);
    }
    
    /**
     * Logs in info level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     */
    public void info(final String className, final String methodName, final String message)
    {
        this.logger.info(className + " in " + methodName + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in warn level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     */
    public void warn(final String className, final String methodName, final String message)
    {
        this.logger.warn(className + " in " + methodName + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in error level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     */
    public void error(final String className, final String methodName, final String message)
    {
        this.logger.error(className + " in " + methodName + ": " + message);
        updated.set(true);
    }
    
    /**
     * Logs in error level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     * @param e exception stack trace
     */
    public void error(final String className, final String methodName, final String message, final Throwable e)
    {
        this.logger.error(className + " in " + methodName + ": " + message, e);
        updated.set(true);
    }

    /**
     * Logs in fatal level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     */
    public void fatal(final String className, final String methodName, final String message)
    {
        this.logger.fatal(className + " in " + methodName + ": " + message);
        updated.set(true);
    }

    /**
     * Logs in fatal level.
     * @param className name of the caller class
     * @param methodName name of the caller method
     * @param message the log message
     * @param e exception stack trace
     */
    public void fatal(final String className, final String methodName, final String message, final Throwable e)
    {
        this.logger.fatal(className + " in " + methodName + ": " + message, e);
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
}