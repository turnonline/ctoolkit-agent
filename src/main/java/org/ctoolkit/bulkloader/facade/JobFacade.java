package org.ctoolkit.bulkloader.facade;

import com.comvai.services.task.TaskExecutorService;
import org.ctoolkit.bulkloader.UpgradeTask;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;
import org.ctoolkit.bulkloader.client.command.UpgradeAction;
import org.ctoolkit.bulkloader.client.command.UpgradeResponse;
import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class JobFacade
{
    private final Logger log;

    private final TaskExecutorService taskExecutorService;

    private final ChangeSetVersionService changeSetVersionService;

    @Inject
    public JobFacade( Logger logger,
                      TaskExecutorService taskExecutorService,
                      ChangeSetVersionService changeSetVersionService )
    {
        this.log = logger;
        this.taskExecutorService = taskExecutorService;
        this.changeSetVersionService = changeSetVersionService;
    }

    public UpgradeResponse upgrade( UpgradeAction action, HttpServletRequest request )
    {
        // get the current version of the data model
        ChangeSetVersion csv = changeSetVersionService.getCurrentChangeSetVersion();

        // create the very first ChangeProgressSet containing the current version from the data store
        BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo();
        progressInfo.setVersion( csv.getVersion() );

        // start the upgrade task
        log.info( "Starting upgrade job." );
        taskExecutorService.execute( new UpgradeTask( progressInfo ) );
        log.info( "Upgrade job started." );

        return new UpgradeResponse();
    }
}
