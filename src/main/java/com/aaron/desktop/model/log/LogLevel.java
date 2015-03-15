/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aaron.desktop.model.log;

/**
 *
 * @author Aaron
 */
public enum LogLevel 
{
    DEBUG("[DEBUG]"), 
    INFO("[INFO]"), 
    WARN("[WARN]"), 
    ERROR("[ERROR]"), 
    FATAL("[FATAL]");

    private final String value;

    /**
     * Gets the value of the log level constant.
     * @return the value of the log level constant
     */
    private LogLevel(final String value)
    {
        this.value = value;
    }

    /**
     * Overrides toString method.
     * @return the value of the log level constant
     */
    @Override
    public String toString()
    {
        return this.value;
    }
    
}
