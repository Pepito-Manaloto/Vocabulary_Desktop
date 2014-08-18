/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.others;

import vocab.model.others.ApplicationLock;
import vocab.model.db.VocabularyRecord;

/**
 * Class that runs after the application is terminated. Closes and frees all resources.
 * @author Aaron
 */
public final class ShutDownHookHandler extends Thread
{
    private final VocabularyRecord vRecord;
    private final ApplicationLock appLock;    
    
    /**
     * Constructor.
     * @param appLock class that handles file lock.
     * @param vRecord class that handles database transactions.
     */
    public ShutDownHookHandler(final ApplicationLock appLock, final VocabularyRecord vRecord)
    {
        this.vRecord = vRecord;
        this.appLock = appLock;
    }
    
    /**
     * Close database connection and release file lock resources.
     */
    @Override
    public void run()
    {
        this.appLock.releaseResources();
        this.vRecord.closeDB();
    }
}
