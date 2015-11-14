package com.aaron.desktop.main;

import com.aaron.desktop.config.Config;
import com.aaron.desktop.view.ViewManager;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import com.aaron.desktop.view.MainFrameView;
import com.aaron.desktop.controller.AddPanelController;
import com.aaron.desktop.controller.MainFrameController;
import com.aaron.desktop.controller.ViewPanelController;
import com.aaron.desktop.model.db.VocabularyRecord;
import com.aaron.desktop.model.others.ApplicationLock;
import com.aaron.desktop.model.others.ShutDownHookHandler;
import com.aaron.desktop.view.AddPanelView;
import com.aaron.desktop.view.ViewPanelView;

import static com.aaron.desktop.main.Main.ManifestAttribute.Implementation_Vendor;
import static com.aaron.desktop.main.Main.ManifestAttribute.Implementation_Version;
import static com.aaron.desktop.main.Main.ManifestAttribute.Specification_Title;
import static com.aaron.desktop.main.Main.ManifestAttribute.Specification_Version;
import com.aaron.desktop.model.others.Mailer;
import com.aaron.desktop.model.others.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

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

    private static final Properties properties;

    static
    {
        try
        {
            properties = new Properties();
            properties.load(new FileInputStream("conf/vocabulary.conf"));

            properties.entrySet().stream().forEach((entry) ->
            {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            });
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }

    }

    /**
     * Initialize the vocabulary frame.
     * @param args
     */
    public static void main(String[] args)
    {
        // Set log4j2 async property
        System.setProperty("log4j.configurationFile", properties.getProperty(Config.LOG4J_CONF.toString()));
        System.setProperty("DLog4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

        final VocabularyRecord vRecord = new VocabularyRecord();
        final Mailer mailer = new Mailer(properties.getProperty(Config.EMAIL_SENDER.toString()),
                                         properties.getProperty(Config.EMAIL_RECIPIENT.toString()));
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
                        MainFrameController mainController = new MainFrameController(mainView, vRecord, mailer);

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