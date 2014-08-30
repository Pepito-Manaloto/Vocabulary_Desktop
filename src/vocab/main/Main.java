package vocab.main;

import vocab.view.ViewManager;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.xml.crypto.dsig.Manifest;
import vocab.model.db.MySQLConnector;
import vocab.view.MainFrameView;
import org.apache.log4j.xml.DOMConfigurator;
import vocab.controller.AddPanelController;
import vocab.controller.MainFrameController;
import vocab.controller.ViewPanelController;
import static vocab.main.Main.ManifestAttribute.Implementation_Vendor;
import static vocab.main.Main.ManifestAttribute.Implementation_Version;
import static vocab.main.Main.ManifestAttribute.Specification_Title;
import static vocab.main.Main.ManifestAttribute.Specification_Version;
import vocab.model.db.VocabularyRecord;
import vocab.model.others.ApplicationLock;
import vocab.model.others.ShutDownHookHandler;
import vocab.view.AddPanelView;
import vocab.view.ViewPanelView;

/**
 * Main class of Main.
 *
 * @author Aaron
 */
public final class Main
{
    /**
     * Attributes of manifests.
     */
    public enum ManifestAttribute
    {
        Specification_Title, 
        Specification_Version,
        Specification_Vendor,
        Implementation_Title, 
        Implementation_Version,
        Implementation_Vendor,
    }
    
    /**
     * Initialize the vocabulary frame.
     * @param args
     */
    public static void main(String[] args)
    {
        if(true == init())
        {
            final VocabularyRecord vRecord = new VocabularyRecord();
            final ApplicationLock appLock = new ApplicationLock();
    
            appLock.lockApplication("Vocabulary is already running.");
            Runtime.getRuntime().addShutdownHook(new ShutDownHookHandler(appLock, vRecord));

            EventQueue.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ViewManager.init();

                            AddPanelView addPanel = new AddPanelView();  
                            ViewPanelView viewPanel = new ViewPanelView();
                            AddPanelController addController = new AddPanelController(addPanel, vRecord);
                            ViewPanelController viewController = new ViewPanelController(viewPanel, vRecord);

                            MainFrameView mainView = new MainFrameView(addPanel, viewPanel);
                            MainFrameController mainController = new MainFrameController(mainView, vRecord);
                            
                            addController.addListeners();
                            viewController.addListeners();
                            mainController.addListeners();
                            
                            mainView.setLocationRelativeTo(null);
                            mainView.setVisible(true);
                        }
                    });  
        }
    }

    /**
     * Initializes log4g.
     * @return true on success, else false
     */
    public static boolean init()
    {
        boolean result = MySQLConnector.setProperties();

        try
        {
            DOMConfigurator.configure("conf/vocabulary-log4j.xml");
        }
        catch(final NullPointerException e)
        {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }
    
    /**
     * Gets the application's information based on the manifest file.
     * @param object class to check the application's information.
     * @return the application's information in a Map
     */
    public static Map<ManifestAttribute, String> getApplicationInfo(final Class<? extends Main> object)
    {
        // Navigate from its class object to a package object
        Package objPackage = object.getPackage();

        String title = objPackage.getSpecificationTitle();
        String version = objPackage.getSpecificationVersion();
        String buildVersion = objPackage.getImplementationVersion();
        String author = objPackage.getImplementationVendor();
        
        if(!version.matches("\\d+\\.?\\d*"))
        {
            JOptionPane.showMessageDialog(null, "Invalid version in manifest.", "Message", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        Map<ManifestAttribute, String> map = new HashMap<>();
        map.put(Specification_Title, title);
        map.put(Specification_Version, version);
        map.put(Implementation_Version, buildVersion);
        map.put(Implementation_Vendor, author);

        return map;
    }
}