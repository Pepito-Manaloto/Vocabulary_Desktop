/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.constant;

/**
 *
 * @author Aaron
 */
public enum ForeignLanguageName
{   
    Hokkien(1), 
    Japanese(2), 
    Mandarin(3);

    private final int id;

    private ForeignLanguageName(final int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
    
    @Override
    public String toString()
    {
        return name();
    }
}
