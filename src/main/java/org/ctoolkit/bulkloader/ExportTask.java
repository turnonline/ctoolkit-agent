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
 * Task creating export task
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ExportTask
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
    public ExportTask( BulkLoaderProgressInfo progressInfo )
    {
        super( "export", progressInfo );
    }

    @Override
    public void run()
    {
        injector.injectMembers( this );

        try
        {
            log.info( "Fetching progress info from parameter map." );

            log.info( "Progress info: " + progressInfo.toString() );
            // call the main functionality, update the data store

            log.info( "Executing export task..." );
            progressInfo = bulkLoader.doExport( progressInfo );

            log.info( "Export task finished with progressInfo: " + progressInfo.toString() );
            // check if the upgrade progress is over
            if ( !ProgressState.DONE.equals( progressInfo.getState() ) )
            {
                // re-schedule the next upgrade task
                log.info( "Scheduling next export task." );
                taskExecutorService.execute( new ExportTask( progressInfo ) );
                log.info( "Export task scheduled" );
            }
            else
            {
                log.info( "Export done." );
            }
        }
        catch ( BulkLoaderException e )
        {
            log.log( Level.SEVERE, "Error occur when exporting data", e );
        }
    }
}
