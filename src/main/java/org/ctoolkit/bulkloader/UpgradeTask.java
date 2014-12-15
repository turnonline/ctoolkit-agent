package org.ctoolkit.bulkloader;

import com.comvai.services.task.TaskExecutorService;
import com.google.inject.Injector;
import org.ctoolkit.bulkloader.common.BulkLoader;
import org.ctoolkit.bulkloader.common.BulkLoaderException;
import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;
import org.ctoolkit.bulkloader.common.ProgressState;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Task for upgrading database model
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class UpgradeTask
        extends BulkLoaderTask
{
    private static final long serialVersionUID = 1L;

    @Inject
    private static Injector injector;

    @Inject
    transient private Logger log;

    @Inject
    transient private BulkLoader bulkLoader;

    @Inject
    transient private TaskExecutorService taskExecutorService;

    /**
     * Constructor
     *
     * @param progressInfo Parameters to set up
     */
    public UpgradeTask( BulkLoaderProgressInfo progressInfo )
    {
        super( "upgrade", progressInfo );
        // it's fine to have the same ID for all as this task is always handled by an admin
        setOwnerId( 10L );
    }

    @Override
    public void run()
    {
        injector.injectMembers( this );

        try
        {
            // call the main functionality, update the data store
            progressInfo = bulkLoader.doUpgrade( progressInfo );

            // check if the upgrade progress is over
            if ( !ProgressState.DONE.equals( progressInfo.getState() ) )
            {
                // re-schedule the next upgrade task
                taskExecutorService.execute( new UpgradeTask( progressInfo ) );
            }
        }
        catch ( BulkLoaderException e )
        {
            log.log( Level.SEVERE, "Error occur when upgrading data model", e );
        }
    }
}
