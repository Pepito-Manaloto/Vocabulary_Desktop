package vocab.main;

import vocab.view.ViewManager;
import java.awt.EventQueue;
import vocab.model.db.MySQLConnector;
import vocab.view.MainFrameView;
import org.apache.log4j.xml.DOMConfigurator;
import vocab.controller.AddPanelController;
import vocab.controller.MainFrameController;
import vocab.controller.ViewPanelController;
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
            DOMConfigurator.configure("vocabulary-log4j.xml");
        }
        catch(final NullPointerException e)
        {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }
}