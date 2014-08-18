/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.view;

/**
 *
 * @author Aaron
 */
public enum ForeignLanguage
{
    Hokkien(1), 
    Japanese(2), 
    Mandarin(3);
    
    private final int id;
    
    private ForeignLanguage(final int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return this.id;
    }
}
