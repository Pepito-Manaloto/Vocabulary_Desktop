/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import vocab.model.log.LogManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
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

    private static ComboPooledDataSource comboPooledDataSource;

    public static final String TEST_QUERY = "SELECT 1";
    private static final LogManager logger = LogManager.getInstance();

    static
    {
        setProperties();
        initComboPooledDataSource();
    }
    
    private MySQLConnector()
    {}

    /**
     * Sets MySQL properties from the property file.
     * @return true on success, else false
     */
    private static boolean setProperties()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream("conf/vocabulary.conf"));

            user = properties.getProperty(Config.DB_USER.toString());
            pw = properties.getProperty(Config.DB_PASS.toString());
            url = properties.getProperty(Config.DB_URL.toString());
            driver = properties.getProperty(Config.DB_DRIVER.toString());
        }
        catch(final IOException ex)
        {
            logger.fatal(MySQLConnector.class.getSimpleName(), "setProperties()", ex.toString(), ex);
            return false;
        }
        
        return true;
    }

    /**
     * Initializes the C3P0 connection pool data source.
     * @return true on success, else false
     */
    private static boolean initComboPooledDataSource()
    {
        try 
        {
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass(driver);
            comboPooledDataSource.setJdbcUrl(url);
            comboPooledDataSource.setUser(user);
            comboPooledDataSource.setPassword(pw);
            
            //Optional params
            comboPooledDataSource.setMinPoolSize(3);
            comboPooledDataSource.setMaxPoolSize(20);
            comboPooledDataSource.setTestConnectionOnCheckin(true);
            comboPooledDataSource.setIdleConnectionTestPeriod(15);
            comboPooledDataSource.setPreferredTestQuery(TEST_QUERY);
        }
        catch(final PropertyVetoException ex)
        {
            logger.fatal(MySQLConnector.class.getSimpleName(), "initComboPooledDataSource()", ex.toString(), ex);
            return false;
        }

        return true;
    }
    
    /**
     * Gets a connection from the database.
     * @return a MySQL JDBC connection
     */
    public static Connection getConnection()
    {            
        Connection con = null;

        try
        {
            con = comboPooledDataSource.getConnection();
        }
        catch(final SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Note", JOptionPane.INFORMATION_MESSAGE);
            logger.fatal(MySQLConnector.class.getSimpleName(), "connect()", ex.toString(), ex);
        }

        return con;
    }
    
    /**
     * Closes the db pool connection.
     */
    public static void closeDB()
    {
        comboPooledDataSource.close();
    }
}