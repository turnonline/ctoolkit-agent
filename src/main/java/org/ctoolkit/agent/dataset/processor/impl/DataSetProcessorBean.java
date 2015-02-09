package org.ctoolkit.agent.dataset.processor.impl;

import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.processor.DataSet;
import org.ctoolkit.agent.dataset.processor.DataSetProcessor;
import org.ctoolkit.agent.dataset.processor.ProgressInfo;
import org.ctoolkit.agent.dataset.reader.ChangeSetVersionService;
import org.ctoolkit.agent.dataset.reader.DataSetReadingStrategy;
import org.ctoolkit.agent.dataset.reader.DataSetReadingStrategyFactory;
import org.ctoolkit.agent.datastore.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The datastore data set processor engine implementation.
 * <p/>
 * Note: no direct access to underlying datastore but through {@link DataStore} interface.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DataSetProcessorBean
        implements DataSetProcessor
{
    private static final Logger log = LoggerFactory.getLogger( DataSetProcessorBean.class );

    private final DataStore dataStore;

    private final ChangeSetVersionService changeSetVersionService;

    private final DataSetReadingStrategyFactory strategyFactory;

    @Inject
    public DataSetProcessorBean( DataSetReadingStrategyFactory strategyFactory,
                                 DataStore dataStore,
                                 ChangeSetVersionService changeSetVersionService )
    {
        this.strategyFactory = strategyFactory;
        this.dataStore = dataStore;
        this.changeSetVersionService = changeSetVersionService;
    }

    @Override
    public void upgrade( @Nonnull Long dataSetId, @Nullable Long notificationId )
    {
        checkNotNull( dataSetId );

        dataStore.addToQueue( new UpgradeTask( dataSetId, notificationId ) );
    }

    @Override
    public ProgressInfo upgrade( @Nonnull ProgressInfo progress, @Nonnull DataSet dataSet ) throws AgentException
    {
        checkNotNull( progress );
        checkNotNull( progress.getVersion() );
        checkNotNull( dataSet );

        log.info( "Upgrade progress: " + progress );

        String pattern = dataSet.getPattern();
        Long version = progress.getVersion();

        // set the cursor in the data set file to the - not implemented yet
        // next change set chunk with version up.getVersion()
        DataSetReadingStrategy reader = strategyFactory.createDataSetReader( version, pattern );

        return applyDataSetOnDataStore( reader, progress );
    }

    private ProgressInfo applyDataSetOnDataStore( DataSetReadingStrategy reader, ProgressInfo prevProgress )
            throws AgentException
    {
        ProgressInfo progressInfo = new ProgressInfo( prevProgress );

        if ( reader.hasNext() )
        {
            // read the next change set
            ChangeSet nextChangeSet = reader.next();

            // apply the change set
            dataStore.applyChangeSet( nextChangeSet );

            // update the progress info
            progressInfo.setVersion( nextChangeSet.getVersion() );
            progressInfo.setSubVersion( nextChangeSet.getSubVersion() == null ? Long.valueOf( 0 ) : nextChangeSet.getSubVersion() );
            progressInfo.setCursor( reader.getCursor() );

            // if this record has been the last one, update the version info and set state as COMPLETED
            if ( !reader.hasNext() )
            {
                progressInfo.setState( ProgressInfo.State.COMPLETED );
                // set the version info
                changeSetVersionService.saveChangeSetVersionInfo( nextChangeSet );
            }
            else
            {
                // there are more records to process
                // set the state to RUNNING
                progressInfo.setState( ProgressInfo.State.RUNNING );
            }
        }
        else
        {
            progressInfo.setState( ProgressInfo.State.COMPLETED );
        }

        log.info( "current progress state: " + progressInfo );
        return progressInfo;
    }
}
