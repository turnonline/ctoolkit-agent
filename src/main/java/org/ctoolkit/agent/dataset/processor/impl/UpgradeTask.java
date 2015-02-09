package org.ctoolkit.agent.dataset.processor.impl;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.processor.ChangeSetVersion;
import org.ctoolkit.agent.dataset.processor.DataSet;
import org.ctoolkit.agent.dataset.processor.DataSetProcessor;
import org.ctoolkit.agent.dataset.processor.ProgressInfo;
import org.ctoolkit.agent.dataset.processor.UpgradeCompletedEvent;
import org.ctoolkit.agent.dataset.reader.ChangeSetVersionService;
import org.ctoolkit.agent.datastore.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The task as asynchronous wrapper of database model upgrade.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class UpgradeTask
        implements DeferredTask
{
    private static final long serialVersionUID = 673174430861675154L;

    private static final Logger logger = LoggerFactory.getLogger( UpgradeTask.class );

    @Inject
    private static Injector injector;

    private final Long dataSetId;

    private final Long notificationId;

    @Inject
    transient private ChangeSetVersionService changeSetVersionService;

    @Inject
    transient private DataSetProcessor processor;

    @Inject
    transient private DataStore dataStore;

    @Inject
    transient private EventBus eventBus;

    public UpgradeTask( @Nonnull Long dataSetId, @Nullable Long notificationId )
    {
        this.dataSetId = checkNotNull( dataSetId );
        this.notificationId = notificationId;
    }

    @Override
    public void run()
    {
        injector.injectMembers( this );

        DataSet dataSet = dataStore.getDataSet( dataSetId );

        if ( dataSet == null )
        {
            logger.warn( "No DataSet has found for Id = " + dataSetId + " Notification Id = " + notificationId );
            return;
        }

        // get the current version of the data model
        ChangeSetVersion csv = changeSetVersionService.getCurrentChangeSetVersion();

        ProgressInfo progress = new ProgressInfo();
        progress.setVersion( csv.getVersion() );

        try
        {
            progress = processor.upgrade( progress, dataSet );

            while ( !progress.isCompleted() )
            {
                progress = processor.upgrade( progress, dataSet );
            }

            eventBus.post( new UpgradeCompletedEvent( dataSet, notificationId ) );
        }
        catch ( AgentException e )
        {
            logger.error( "Processing has failed for DataSet.Id = "
                    + dataSetId + " Notification Id = " + notificationId, e );
        }
    }
}
