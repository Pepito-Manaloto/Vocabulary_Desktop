package vocab.main;

import vocab.view.ViewManager;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import vocab.view.MainFrameView;
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

    static
    {
        // Set log4j2 async property
        System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
        System.setProperty("DLog4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    }

    /**
     * Initialize the vocabulary frame.
     * @param args
     */
    public static void main(String[] args)
    {
        final VocabularyRecord vRecord = new VocabularyRecord();
        ApplicationLock appLock = new ApplicationLock();

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
        
        if(version == null || !version.matches("\\d+\\.?\\d*"))
        {
            JOptionPane.showMessageDialog(null, "Invalid version in manifest.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }

        Map<ManifestAttribute, String> map = new HashMap<>();
        map.put(Specification_Title, title);
        map.put(Specification_Version, version);
        map.put(Implementation_Version, buildVersion);
        map.put(Implementation_Vendor, author);

        return map;
    }
}