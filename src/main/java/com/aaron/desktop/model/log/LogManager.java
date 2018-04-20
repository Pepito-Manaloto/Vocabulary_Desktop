/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Aaron
 */
public final class LogManager
{    
    private static final LogManager LOG_MANAGER = new LogManager();
    private static final AtomicBoolean UPDATED = new AtomicBoolean(); // In-case multiple threads use this flag.
    private static final String LOG_FILE = "logs/vocabulary.log";

    private final Logger logger;
 
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
        return UPDATED.get();
    }
    
    /**
     * Sets the log file update flag to false.
     */
    public static void finishedUpdating()
    {
        UPDATED.set(false);
    }
    
    /**
     * Gets a LogManager instance, ensures singleton.
     * @return LogManager instance
     */
    public static LogManager getInstance()
    {
        return LOG_MANAGER;
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
       UPDATED.set(true);
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
        UPDATED.set(true);
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
        UPDATED.set(true);
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
        UPDATED.set(true);
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
        UPDATED.set(true);
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
        UPDATED.set(true);
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
        UPDATED.set(true);
    }

    /**
     * Gets all messages in the log and stores it to a map with the message as key.
     *
     * @throws IOException
     * @return the log messages
     */
    public LinkedHashMap<String, LogLevel> getMessageLogFromFile() throws IOException
    {
        LinkedHashMap<String, LogLevel> messageList = new LinkedHashMap<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_FILE), StandardCharsets.UTF_8));)
        {
            String line;

            while((line = br.readLine()) != null) //Gets each message
            {
                putLogLineAndLevelInMessageList(messageList, line);
            }
        }

        return messageList;
    }
    
    private void putLogLineAndLevelInMessageList(LinkedHashMap<String, LogLevel> messageList, String logLine)
    {
        for(LogLevel level: LogLevel.values()) //Puts message as the key and its log level as value
        {
            if(logLine.contains(level.toString()))
            {
                messageList.put(logLine, level);
            }
        }
    }
}