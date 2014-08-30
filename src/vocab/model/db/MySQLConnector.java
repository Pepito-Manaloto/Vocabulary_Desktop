/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.db;

import vocab.model.log.LogManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import vocab.config.Config;

/**
 *
 * @author Aaron
 */
public final class MySQLConnector 
{
    private static String user;
    private static String pw;    
    private static String url;
    private static String driver;
    
    private final static LogManager logger = LogManager.getInstance();
    
    private MySQLConnector(){};
    
    /**
     * Sets MySQL properties from the property file.
     * @return true on success, else false
     */
    public static boolean setProperties()
    {
        boolean result;
        
        try
        {
            final Properties properties = new Properties();
            properties.load(new FileInputStream("conf/vocabulary.conf"));

            user = properties.getProperty(Config.DB_USER.toString());
            pw = properties.getProperty(Config.DB_PASS.toString());
            url = properties.getProperty(Config.DB_URL.toString());
            driver = properties.getProperty(Config.DB_DRIVER.toString());

            result = true; 
        }
        catch (final IOException ex)
        {
            result = false;
            logger.fatal(ex.toString());
        }
        
        return result;
    }
    
    /**
     * Gets a connection from the database. Uses a hard coded username an password.
     * @return a MySQL JDBC connection
     */
    public static Connection connect()  
    {            
        Connection con = null;

        try
        {
            con = DriverManager.getConnection(url, user, pw);
        }
        catch(final SQLException ex)
        {        ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Note", JOptionPane.INFORMATION_MESSAGE);
            logger.fatal(ex.toString());
        }

        return con;
    }
}