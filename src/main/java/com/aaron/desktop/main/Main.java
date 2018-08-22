package com.aaron.desktop.main;

import com.aaron.desktop.constant.Config;
import com.aaron.desktop.view.ViewManager;
import java.awt.EventQueue;
import com.aaron.desktop.view.MainFrameView;
import com.aaron.desktop.controller.AddPanelController;
import com.aaron.desktop.controller.MainFrameController;
import com.aaron.desktop.controller.ViewPanelController;
import com.aaron.desktop.model.db.VocabularyRecord;
import com.aaron.desktop.model.others.ApplicationLock;
import com.aaron.desktop.model.others.ShutDownHookHandler;
import com.aaron.desktop.view.AddPanelView;
import com.aaron.desktop.view.ViewPanelView;
import com.aaron.desktop.model.others.Mailer;

/**
 * Main class of Main.
 *
 * @author Aaron
 */
public final class Main
{
    /**
     * Initialize the vocabulary frame.
     * @param args
     */
    public static void main(String[] args)
    {
        setLog4j2AsyncProperties();

        VocabularyRecord vRecord = new VocabularyRecord();
        Mailer mailer = new Mailer(PropertiesConfig.getProperty(Config.EMAIL_SENDER),
                                   PropertiesConfig.getProperty(Config.EMAIL_RECIPIENT));

        ApplicationLock appLock = lockApplication();
        Runtime.getRuntime().addShutdownHook(new ShutDownHookHandler(appLock, vRecord));

        initializeViewControllerAndAddListeners(vRecord, mailer);
    }

    private static void setLog4j2AsyncProperties()
    {
        // Set log4j2 async property
        System.setProperty("log4j.configurationFile", PropertiesConfig.getProperty(Config.LOG4J_CONF));
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
}