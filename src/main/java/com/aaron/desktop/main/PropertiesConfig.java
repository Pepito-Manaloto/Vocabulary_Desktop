/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.main;

import com.aaron.desktop.constant.Config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Aaron
 */
public final class PropertiesConfig
{
    private static final Properties PROPERTIES;

    static
    {
        try
        {
            PROPERTIES = new Properties();
            PROPERTIES.load(new FileInputStream("conf/vocabulary.conf"));

            for(Map.Entry<Object, Object> entry : PROPERTIES.entrySet())
            {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
        catch (IOException ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static String getProperty(Config configKey)
    {
        return PROPERTIES.getProperty(configKey.toString());
    }
}
