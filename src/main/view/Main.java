package main.view;

import java.awt.EventQueue;
import main.model.MySQLConnector;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Main class of Main.
 *
 * @author Aaron
 */
final class Main
{
    /**
     * Initialize the vocabulary frame.
     */
    public static void main(String[] args)
    {
        if(true == init())
        {
            EventQueue.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ViewManager.init();
                            MainFrame vocabulary = new MainFrame();
                            vocabulary.setLocationRelativeTo(null);
                            vocabulary.setVisible(true);
                        }
                    });  
        }
    }

    public static boolean init()
    {
        boolean result = MySQLConnector.setProperties();

        try
        {
            DOMConfigurator.configure("vocabulary-log4j.xml");
        }
        catch(final NullPointerException e)
        {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }
 /*
    private static String encryptUsingSHA256(String stringToEncrypt) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); 

        md.update(stringToEncrypt.getBytes()); 
        
        byte b[] = md.digest();
        
        //convert the byte to hex
        StringBuilder sb = new StringBuilder        
        
        for (int i = 0; i < b.length; i++) 
            sb.append( Integer.toString( (b[i] & 0xff) + 0x100, 16).substring(1) );
   
        return sb.toString();
    }
*/
}