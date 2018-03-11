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
import org.apache.commons.lang3.StringUtils;

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

    private static final Properties PROPERTIES;
    private static final Map<ManifestAttribute, String> APPLICATION_INFO_MAP;

    static
    {
        APPLICATION_INFO_MAP = initializeApplicationInfoMap();

        try
        {
            PROPERTIES = new Properties();
            PROPERTIES.load(new FileInputStream("conf/vocabulary.conf"));

            PROPERTIES.entrySet().stream().forEach((entry) ->
            {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            });
        }
        catch (IOException ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Map<ManifestAttribute, String> initializeApplicationInfoMap()
    {
        Map<ManifestAttribute, String> infoMap = new EnumMap<>(ManifestAttribute.class);
        // Navigate from its class object to a package object
        Package objPackage = Main.class.getPackage();

        String title = objPackage.getSpecificationTitle();
        String version = objPackage.getSpecificationVersion();
        String buildVersion = objPackage.getImplementationVersion();
        String author = objPackage.getImplementationVendor();

        if(StringUtils.isNotBlank(title))
        {
            infoMap.put(Specification_Title, title);
        }

        if(StringUtils.isNotBlank(version))
        {
            infoMap.put(Specification_Version, version);
        }

        if(StringUtils.isNotBlank(buildVersion))
        {
            infoMap.put(Implementation_Version, buildVersion);
        }

        if(StringUtils.isNotBlank(author))
        {
            infoMap.put(Implementation_Vendor, author);
        }
        
        return infoMap;
    }

    /**
     * Initialize the vocabulary frame.
     * @param args
     */
    public static void main(String[] args)
    {
        setLog4j2AsyncProperties();

        VocabularyRecord vRecord = new VocabularyRecord();
        Mailer mailer = new Mailer(PROPERTIES.getProperty(Config.EMAIL_SENDER.toString()),
                                         PROPERTIES.getProperty(Config.EMAIL_RECIPIENT.toString()));

        ApplicationLock appLock = lockApplication();
        Runtime.getRuntime().addShutdownHook(new ShutDownHookHandler(appLock, vRecord));

        initializeViewControllerAndAddListeners(vRecord, mailer);
    }

    private static void setLog4j2AsyncProperties()
    {
        // Set log4j2 async property
        System.setProperty("log4j.configurationFile", PROPERTIES.getProperty(Config.LOG4J_CONF.toString()));
        System.setProperty("DLog4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    }
    
    private static ApplicationLock lockApplication()
    {
        ApplicationLock appLock = new ApplicationLock();
        appLock.lockApplication("Vocabulary is already running.");
        
        return appLock;
    }
    
    private static void initializeViewControllerAndAddListeners(final VocabularyRecord vRecord, final Mailer mailer)
    {
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