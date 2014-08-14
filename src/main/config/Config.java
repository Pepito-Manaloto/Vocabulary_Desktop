/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.config;

/**
 * Configuration property name constants.
 * @author Aaron
 */
public enum Config
{
    DB_URL("db.url"),
    DB_DRIVER("db.driver"),
    DB_USER("db.user"),
    DB_PASS("db.pass"),
    
    DOM_LOG4J_CONFIG("dom.log4j.config");
    
    /** The value of the Config constant. */
    private final String value;

    /**
     * Constructor for creating the Config enum.
     * @param value value of config enum
     */
    private Config(final String value)
    {
        this.value = value;
    }

    /**
     * Gets the value of the Config constant.
     * @return the value of the Config constant
     */
    public String value()
    {
        return this.value;
    }

    /**
     * Overrides toString method.
     * @return the value of the Config constant
     */
    @Override
    public String toString()
    {
        return this.value;
    }
}