/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model;

/**
 *
 * @author Aaron
 */
public enum ForeignLanguage
{
    HOKKIEN("Hokkien"), 
    JAPANESE("Japanese"), 
    MANDARIN("Mandarin");
    
    private final String nameAsString;
    
    private ForeignLanguage(final String nameAsString)
    {
        this.nameAsString = nameAsString;
    }
    
    @Override
    public String toString()
    {
        return this.nameAsString;
    }
}
