/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.others;

import vocab.model.log.LogManager;
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
     * Class empty constructor.
     */
    public CommandLineScript()
    {}
    
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

            if(result == 0)
            {
                successful = true;
            }                    
        }
        catch (final IOException | InterruptedException ex)
        {
            successful = false;
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

            if(result == 0)
            {
                successful = true;
            }                    
        }
        catch (final IOException | InterruptedException ex)
        {
            successful = false;
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

            if(result == 0)
            {
                successful = true;
            }                    
        }
        catch (final IOException | InterruptedException ex)
        {
            successful = false;
            this.errorMessage = ex.toString() + " error code=" + result;
            this.logger.error(this.className, "execute(String[], String)", ex.toString(), ex);
        }
            
        return successful;
    }
    
    /**
     * Returns the error message from execute() method.
     * @return the error message, if there is, otherwise empty message
     */
    public String getErrorMessage()
    {
        if(null != this.errorMessage)
        {
            return this.errorMessage;
        }       
        else 
        {  
            return "";
        }
    }
}
