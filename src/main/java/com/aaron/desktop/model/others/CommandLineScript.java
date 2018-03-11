/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.others;

import static java.util.Objects.*;

import com.aaron.desktop.model.log.LogManager;
import java.io.File;
import java.io.IOException;

/**
 * Handles command line scripts.
 * @author Aaron
 */
public final class CommandLineScript
{
    private Process runtimeProcess;
    private String errorMessage;
    
    private final LogManager logger = LogManager.getInstance();
    private final String className = this.getClass().getSimpleName();
    
    /**
     * Executes the given array of scripts.
     * @param script array of scripts
     * @return true if successful, otherwise false
     */
    public boolean execute(final String... script)
    {
        boolean successful = false;
        int result = 0;

        try
        {
            this.runtimeProcess = Runtime.getRuntime().exec(script);
            result = this.runtimeProcess.waitFor(); // Returns the result of the runtime execution.
            successful = parseProcessExitStatus(result);
        }
        catch(final IOException | InterruptedException ex)
        {
            this.errorMessage = ex.toString() + " error code=" + result;
            this.logger.error(this.className, "execute(String...)", ex.toString(), ex);
        }
            
        return successful;
    }
    
    /**
     * Executes the given script, and sends output to a file.
     * @param script array of scripts
     * @param outputPath the path where the output file will be saved
     * @return true if successful, otherwise false
     */
    public boolean execute(final String script, final String outputPath)
    {
        boolean successful = false;
        File path = new File(outputPath);
        int result = 0;

        try
        {
            this.runtimeProcess = Runtime.getRuntime().exec(script, null, path);
            result = this.runtimeProcess.waitFor(); // Returns the result of the runtime execution.
            successful = parseProcessExitStatus(result);
        }
        catch(final IOException | InterruptedException ex)
        {
            this.errorMessage = ex.toString() + " error code=" + result;
            this.logger.error(this.className, "execute(String, String)", ex.toString(), ex);
        }

        return successful;
    }
    
    /**
     * Executes the given array of scripts, and sends output to a file.
     * @param script array of scripts
     * @param outputPath the path where the output file will be saved
     * @return true if successful, otherwise false
     */
    public boolean execute(final String[] script, final String outputPath)
    {
        boolean successful = false;
        File path = new File(outputPath);
        int result = 0;

        try
        {
            this.runtimeProcess = Runtime.getRuntime().exec(script, null, path);
            result = this.runtimeProcess.waitFor(); // Returns the result of the runtime execution.
            successful = parseProcessExitStatus(result);
        }
        catch(final IOException | InterruptedException ex)
        {
            this.errorMessage = ex.toString() + " error code=" + result;
            this.logger.error(this.className, "execute(String[], String)", ex.toString(), ex);
        }
            
        return successful;
    }
    
    private boolean parseProcessExitStatus(int result)
    {
        return result == 0;
    }
    
    /**
     * Returns the error message from execute() method.
     * @return the error message, if there is, otherwise empty message
     */
    public String getErrorMessage()
    {
        if(nonNull(errorMessage))
        {
            return this.errorMessage;
        }       
        else 
        {  
            return "";
        }
    }
}
