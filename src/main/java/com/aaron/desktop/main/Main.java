package com.aaron.desktop.main;

import com.aaron.desktop.config.Config;
import com.aaron.desktop.view.ViewManager;
import java.awt.EventQueue;
import java.util.Map;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
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
    private static final Map<ManifestAttribute, String> APPLICATION_INFO_MAP;

    static
    {
        APPLICATION_INFO_MAP = new EnumMap<>(ManifestAttribute.class);
        // Navigate from its class object to a package object
        Package objPackage = Main.class.getPackage();

        String title = objPackage.getSpecificationTitle();
        String version = objPackage.getSpecificationVersion();
        String buildVersion = objPackage.getImplementationVersion();
        String author = objPackage.getImplementationVendor();

        if(title != null && !title.isEmpty())
        {
            APPLICATION_INFO_MAP.put(Specification_Title, title);
        }

        if(version != null && !version.isEmpty())
        {
            APPLICATION_INFO_MAP.put(Specification_Version, version);
        }

        if(buildVersion != null && !buildVersion.isEmpty())
        {
            APPLICATION_INFO_MAP.put(Implementation_Version, buildVersion);
        }

        if(author != null && !author.isEmpty())
        {
            APPLICATION_INFO_MAP.put(Implementation_Vendor, author);
        }

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

        EventQueue.invokeLater(() -> {
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
        });
    }

    /**
     * Returns the manifest info about the application.
     * @param attribute the manifest attribute to get
     * @return String
     */
    public static String getInfo(ManifestAttribute attribute)
    {
        String defaultValue;

        switch(attribute)
        {
            case Specification_Title: 
                defaultValue = "Vocabulary";
                break;
            case Specification_Version:
                defaultValue = "7";
                break;
            case Specification_Vendor:
                defaultValue = "Aaron";
                break;
            case Implementation_Title:
                defaultValue = "Vocabulary";
                break;
            case Implementation_Version:
                /*
                git log --oneline | wc -l | gawk '{print $1}'
                git rev-parse --short HEAD
                git rev-parse --abbrev-ref HEAD
                */
                defaultValue = "25-0c3d8a3-hibernate";
                break;
            case Implementation_Vendor:
                defaultValue = "Aaron";
                break;
            default:
                defaultValue = "None";
            
        }
        return APPLICATION_INFO_MAP.getOrDefault(attribute, defaultValue);
    }
}