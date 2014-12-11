package org.ctoolkit.bulkloader;

import com.comvai.services.task.TaskExecutorService;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;
import org.ctoolkit.bulkloader.common.BulkLoaderConstants;
import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;
import org.ctoolkit.bulkloader.conf.Configuration;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.exportstore.ExportStore;
import org.ctoolkit.bulkloader.undostore.ProgressDiary;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Singleton
public class BulkLoaderJobServlet
        extends HttpServlet
{

    public final static String NEWLINE = "<br/>";

    /**
     * Default generated serial version id
     */
    private static final long serialVersionUID = 1L;

    private static JobState state = JobState.IDLE;

    private final TaskExecutorService taskExecutorService;

    private final Logger log;

    private final ChangeSetVersionService changeSetVersionService;

    private final ProgressDiary progressDiary;

    private final ExportStore exportStore;

    private final Configuration configuration;

    @Inject
    public BulkLoaderJobServlet( TaskExecutorService taskExecutorService,
                                 Logger log,
                                 ChangeSetVersionService changeSetVersionService,
                                 ProgressDiary progressDiary,
                                 ExportStore exportStore,
                                 Configuration configuration )
    {
        this.taskExecutorService = taskExecutorService;
        this.log = log;
        this.changeSetVersionService = changeSetVersionService;
        this.progressDiary = progressDiary;
        this.exportStore = exportStore;
        this.configuration = configuration;
    }

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        doPost( req, resp );
    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        doJob( req, resp );
    }

    /**
     * possible parameters:
     * export:
     * mode=export:export-job
     * if no export-job specified, all the jobs are executed
     * the export job is executed, the result is stored in the
     * export store
     * <p/>
     * import:
     * mode=import[:version.subVersion]
     * if no version is set, the most recent copy of the current
     * data model version is read from export store
     * <p/>
     * upgrade model:
     * mode=upgrade[:maxVersion]
     * updates the data model to the given version (if set)
     * the data is read only from the changesets.xml file
     * <p/>
     * basic info:
     * mode=info
     */
    protected void doJob( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        Writer writer = resp.getWriter();
        resp.setContentType( "text/html; charset=UTF-8" );

        // only 1 import/export at time
        /*
        synchronized (this) {
        	if (JobState.IDLE != state) {
        		writer.write("Another import/export task is already running. Quitting now.");
        		return;
        	}
        	state = JobState.RUNNING;
		}
		*/

        String mode = ( String ) req.getParameter( BulkLoaderConstants.COMMAND_MODE );
        if ( null != mode && mode.startsWith( BulkLoaderConstants.COMMAND_MODE_INFO ) )
        {
            ChangeSetVersion csv = changeSetVersionService.getCurrentChangeSetVersion();
            writer.write( "Version info:" + NEWLINE + csv.toString() );
            writer.write( NEWLINE + NEWLINE + "Undo info:" + NEWLINE + progressDiary.getInfo() );
            writer.write( NEWLINE + NEWLINE + "Export info:" + NEWLINE + exportStore.getInfo() );
            return;
        }

        // handle undo data
        // something wrong happened, try to revert the last changes
        ProgressDiary pd = progressDiary;
        if ( !pd.isEmpty() )
        {
            // start and UNDO task
            pd.rollback();
            // return after rollback, need to e restarted
            log.info( "Rollback done, please restart the operation again" );
            writer.write( "Rollback done, please restart the operation again" );
            return;
        }

        if ( null == mode )
        {
            writer.write( "Missing mode parameter..." + NEWLINE );
            writer.write( "Use: mode=export:export_job" + NEWLINE );
            writer.write( "     mode=import" + NEWLINE );
            writer.write( "     mode=upgrade" + NEWLINE );
            writer.write( "     mode=info" + NEWLINE );
        }
        else if ( mode.startsWith( BulkLoaderConstants.COMMAND_MODE_EXPORT ) )
        {
            // read properties from the request
            String exportJobName;
            if ( mode.contains( ":" ) )
            {
                exportJobName = mode.substring( mode.indexOf( ":" ) + 1 );
            }
            else
            {
                writer.write( "Missing export job name" + NEWLINE );
                return;
            }

            ChangeSetVersion csv = changeSetVersionService.getCurrentChangeSetVersion();
            BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo();

            progressInfo.setVersion( csv.getVersion() );
            progressInfo.setProp( BulkLoaderConstants.PROP_EXPORT_JOB, exportJobName );

            // get the first kind
            ExportJob exportJob = configuration.getExportJob( exportJobName );
            if ( null != exportJob && exportJob.hasKinds() )
            {
                progressInfo.setProp( BulkLoaderConstants.PROP_EXPORT_KIND, exportJob.getKinds().getKinds().get( 0 ).getName() );
            }

            writer.write( "Starting export job..." + NEWLINE );
            log.info( "Starting export job '" + exportJobName + "'" );
            taskExecutorService.execute( new ExportTask( progressInfo ) );
            writer.write( "Export job started..." );
            log.info( "Export job started." );
        }
        else if ( mode.startsWith( BulkLoaderConstants.COMMAND_MODE_IMPORT ) )
        {
            // read properties from the request
            String version = "0";
            if ( mode.contains( ":" ) )
            {
                version = mode.substring( mode.indexOf( ":" ) );
            }

            // if version = 0 -> import the last version from the export store
            BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo( Long.valueOf( version ) );

            // start the import task
            writer.write( "Starting import job..." + NEWLINE );
            log.info( "Starting import job." );
            taskExecutorService.execute( new ImportTask( progressInfo ) );
            writer.write( "Import job started..." + NEWLINE );
            log.info( "Import job started." );

        }
        else if ( mode.startsWith( BulkLoaderConstants.COMMAND_MODE_UPGRADE ) )
        {
            // read properties from the request
            if ( mode.contains( ":" ) )
            {
                String version = mode.substring( mode.indexOf( ":" ) );
            }

            // get the current version of the data model
            ChangeSetVersion csv = changeSetVersionService.getCurrentChangeSetVersion();

            // create the very first ChangeProgressSet containing the current version from the data store
            BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo();
            progressInfo.setVersion( csv.getVersion() );

            // start the upgrade task
            writer.write( "Starting upgrade job..." + NEWLINE );
            log.info( "Starting upgrade job." );
            taskExecutorService.execute( new UpgradeTask( progressInfo ) );
            writer.write( "Upgrade job started..." + NEWLINE );
            log.info( "Upgrade job started." );
        }
        //	state = JobState.IDLE;
    }
}
