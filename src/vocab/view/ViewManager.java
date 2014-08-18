/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

/**
 *
 * @author Aaron
 */
public final class ViewManager
{
    public static final Font VERDANA12 = new Font("Verdana", Font.PLAIN, 12);
    public static final Font VERDANA14 = new Font("VERDANA", Font.PLAIN, 14);
    public static final Font VERDANA16 = new Font("Verdana", Font.PLAIN, 16);
    public static final Font MEIRYO14 = new Font("Meiryo UI", Font.PLAIN, 14);
    public static final Font MEIRYO19 = new Font("Meiryo UI", Font.PLAIN, 19);

    /**
     Private constructor to prevent initialization
     */
    private ViewManager()
    {}
    
    /**
     * Set up component's properties.
     */
    public static void init()
    {
        UIManager.put("Button.font", VERDANA14);
        UIManager.put("TextField.font", MEIRYO14);
        UIManager.put("Table.font", MEIRYO14);
        UIManager.put("TableHeader.font", VERDANA14);
        UIManager.put("ComboBox.font", VERDANA12);
        UIManager.put("CheckBox.font", VERDANA12);
        UIManager.put("Label.font", VERDANA12);
        UIManager.put("List.font", MEIRYO14);
        UIManager.put("Menu.font", VERDANA12);
        UIManager.put("MenuItem.font", VERDANA12);
        
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("TableHeader.background", Color.WHITE);
        UIManager.put("ScrollPane.background", Color.WHITE);
        UIManager.put("MenuBar.background", Color.WHITE);
        UIManager.put("Menu.background", Color.WHITE);
        UIManager.put("MenuItem.background", Color.WHITE);
        UIManager.put("CheckBoxMenuItem.background", Color.WHITE);
    }
}