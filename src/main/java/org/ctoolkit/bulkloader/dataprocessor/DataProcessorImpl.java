package org.ctoolkit.bulkloader.dataprocessor;

import org.ctoolkit.bulkloader.changesets.ChangeSetReader;
import org.ctoolkit.bulkloader.changesets.ChangeSetReadingStrategy;
import org.ctoolkit.bulkloader.changesets.ChangeSetReadingStrategyFactory;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.common.BulkLoaderConstants;
import org.ctoolkit.bulkloader.common.BulkLoaderException;
import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;
import org.ctoolkit.bulkloader.common.ProgressState;
import org.ctoolkit.bulkloader.conf.Configuration;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.conf.model.Kind;
import org.ctoolkit.bulkloader.datastore.DataStore;
import org.ctoolkit.bulkloader.datastore.DataStoreCallback;
import org.ctoolkit.bulkloader.exportstore.ExportStore;
import org.ctoolkit.bulkloader.undostore.ProgressDiary;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class DataProcessorImpl
        implements DataProcessor, DataStoreCallback
{

    /**
     * Logger for this class
     */
    private final Logger logger;

    /**
     * Data model descriptor
     */
    private final DataDiff dataModelDiff;

    /**
     * Interface for data store
     */
    private final DataStore dataStore;

    /**
     * Interface for export store
     */
    private final ExportStore exportStore;

    /**
     * Interface for logging progress
     */
    private final ProgressDiary progressDiary;

    /**
     * Interface for changeSetVersionService
     */
    private final ChangeSetVersionService changeSetVersionService;

    /**
     * Interface for configuration
     */
    private final Configuration configuration;

    private final ChangeSetReadingStrategyFactory factory;

    /**
     * Constructor for injector
     *
     * @param dataModelDiff           interface for data model diff
     * @param dataStore               interface for data store
     * @param exportStore             interface to export store
     * @param changeSetVersionService interface for version service
     * @param progressDiary
     * @param configuration
     * @param factory
     * @param logger
     */
    @Inject
    protected DataProcessorImpl( DataDiff dataModelDiff,
                                 DataStore dataStore,
                                 ExportStore exportStore,
                                 ChangeSetVersionService changeSetVersionService,
                                 ProgressDiary progressDiary,
                                 Configuration configuration,
                                 ChangeSetReadingStrategyFactory factory,
                                 Logger logger )
    {
        this.dataModelDiff = dataModelDiff;
        this.dataStore = dataStore;
        this.exportStore = exportStore;
        this.changeSetVersionService = changeSetVersionService;
        this.progressDiary = progressDiary;
        this.configuration = configuration;
        this.factory = factory;
        this.logger = logger;
    }

    /**
     * Method imports version from export store. It goes from current version of
     * the data store to the requested version.
     *
     * @param progressInfo import progress descriptor
     * @return
     */
    @Override
    public BulkLoaderProgressInfo importVersionFromExportStore( BulkLoaderProgressInfo progressInfo )
            throws BulkLoaderException
    {
        logger.info( "Import progress: " + progressInfo );

        ChangeSetReadingStrategy strategy = factory.createExportStore( progressInfo.getMaxVersion(),
                progressInfo.getVersion(), progressInfo.getSubVersion(), progressInfo.getCursor() );

        return applyChangeSetsOnDataStore( new ChangeSetReader( strategy ), progressInfo );
    }

    /**
     * Method reads change sets from change set file and applies them one by one on
     * the data store
     *
     * @param progressInfo progress info
     * @return
     */
    @Override
    public BulkLoaderProgressInfo upgradeToVersion( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException
    {
        logger.info( "Upgrade progress: " + progressInfo );
        // set the cursor in the data set file to the - not implemented yet
        // next change set chunk with version up.getVersion()
        ChangeSetReadingStrategy strategy = factory.createDataSets( progressInfo.getVersion() );
        ChangeSetReader changeSetReader = new ChangeSetReader( strategy );

        return applyChangeSetsOnDataStore( changeSetReader, progressInfo );
    }

    /**
     * @param changeSetReader
     * @param prevProgressInfo
     * @return
     * @throws BulkLoaderException
     */
    private BulkLoaderProgressInfo applyChangeSetsOnDataStore( ChangeSetReader changeSetReader, BulkLoaderProgressInfo prevProgressInfo )
            throws BulkLoaderException
    {
        // register the callback for the data storage
        // to handle the undo operations
        dataStore.registerCallback( this );
        BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo( prevProgressInfo );
        if ( changeSetReader.hasNext() )
        {
            // read the next changeset
            ChangeSet nextChangeSet = changeSetReader.next();

            // apply the change set
            dataStore.applyChangeSet( nextChangeSet );

            // if a version is successfully applied, the undo records has to be removed
            progressDiary.purgeEntries();

            // actualize the progress info
            progressInfo.setVersion( nextChangeSet.getVersion() );
            progressInfo.setSubVersion( null == nextChangeSet.getSubVersion() ? Long.valueOf( 0 ) : nextChangeSet.getSubVersion() );
            progressInfo.setCursor( changeSetReader.getCursor() );

            // if this record was the last one, update the version info
            // purge undo table
            // and set state as DONE
            if ( !changeSetReader.hasNext() )
            {
                progressInfo.setState( ProgressState.DONE );
                // set the version info
                changeSetVersionService.saveChangeSetVersionInfo( nextChangeSet );
            }
            else
            {
                // there are more records to process
                // set the state to RUNNING
                progressInfo.setState( ProgressState.RUNNING );
            }
        }
        else
        {
            progressInfo.setState( ProgressState.DONE );
        }
        logger.info( "current progress state: " + progressInfo );
        return progressInfo;
    }

    /**
     * Method used by roll back. It looks for last full dataStore dump in the
     * blob service imports it, and then goes trough blobs/user version (with
     * priority on blob store export) and imports them one by one
     *
     * @param version version to rebuild
     * @return
     */
    @Override
    public void rebuildVersion( Long version ) throws BulkLoaderException
    {
        // look for the latest full export version in the blobStore
        Long lastFullVersion = getLastFullChangeSetVersion( changeSetVersionService.getCurrentChangeSetVersion().getVersion() );
        /*
          ExportStorePriorityChangeSetReadingStrategy readingStrategy = new ExportStorePriorityChangeSetReadingStrategy(lastFullVersion);
          while (readingStrategy.hasNext()) {
              dataStore.applyChangeSet(readingStrategy.next());
          }
          */
    }

    public void exportCurrentVersion() throws BulkLoaderException
    {
        fullExportCurrentVersion();
    }

    /**
     * @return latest full export version in the blobStore
     */
    private Long getLastFullChangeSetVersion( Long beforeVersion )
    {
        return ( long ) 0;
    }

    /**
     * Method exports the current version and stores it in the export store
     *
     * @return
     */
    @Override
    public BulkLoaderProgressInfo exportCurrentVersion( BulkLoaderProgressInfo prevProgressInfo )
            throws BulkLoaderException
    {
        logger.info( "Export progress: " + prevProgressInfo );
        String job = prevProgressInfo.getProps().get( BulkLoaderConstants.PROP_EXPORT_JOB );
        String kind = prevProgressInfo.getProps().get( BulkLoaderConstants.PROP_EXPORT_KIND );

        ChangeSetReadingStrategy strategy = factory.createDataStore( job, kind, prevProgressInfo.getCursor() );
        ChangeSetReader changeSetReader = new ChangeSetReader( strategy );

        BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo( prevProgressInfo );
        if ( changeSetReader.hasNext() )
        {
            // if the subversion is not set, it means this is the first call
            if ( null == prevProgressInfo.getSubVersion() )
            {
                Long subVersion = exportStore.beginExport( prevProgressInfo.getVersion() );
                progressInfo.setSubVersion( subVersion );
            }
            else
            {
                exportStore.continueExport( prevProgressInfo.getVersion(), prevProgressInfo.getSubVersion() );
            }

            // read the next changeset
            ChangeSet nextChangeSet = changeSetReader.next();

            // apply the change set
            exportStore.saveChangeSet( nextChangeSet );

            // actualize the progress info
            progressInfo.setCursor( changeSetReader.getCursor() );

            // if this record was the last one, finish the export
            // and set state as DONE
            if ( !changeSetReader.hasNext() )
            {
                // check if there is another kind to export
                boolean hasNext = false;
                ExportJob exportJob = configuration.getExportJob( job );
                if ( null != exportJob && exportJob.hasKinds() )
                {
                    for ( int i = 0; i < exportJob.getKinds().getKinds().size(); i++ )
                    {
                        Kind exportJobKind = exportJob.getKinds().getKinds().get( i );
                        if ( exportJobKind.getName().equals( kind ) )
                        {
                            // get and set the next one - if exist
                            if ( exportJob.getKinds().getKinds().size() > i + 1 )
                            {
                                Kind nextKind = exportJob.getKinds().getKinds().get( i + 1 );
                                progressInfo.setProp( BulkLoaderConstants.PROP_EXPORT_KIND, nextKind.getName() );
                                progressInfo.setCursor( "" );
                                hasNext = true;
                                break;
                            }
                        }
                    }
                }

                if ( !hasNext )
                {
                    // if isn't set the state flag to done
                    progressInfo.setState( ProgressState.DONE );
                    // and finish the export
                    exportStore.finishExport( prevProgressInfo.getVersion(), prevProgressInfo.getSubVersion() );
                }
            }
            else
            {
                // there are more records to process
                // set the state to RUNNING
                progressInfo.setState( ProgressState.RUNNING );
            }
        }
        else
        {
            // finish the export
            exportStore.finishExport( prevProgressInfo.getVersion(), prevProgressInfo.getSubVersion() );
            progressInfo.setState( ProgressState.DONE );
        }
        return progressInfo;
    }

    private void fullExportCurrentVersion() throws BulkLoaderException
    {
        ChangeSet changeSet = dataModelDiff.getNextDiff();
        if ( !changeSet.isEmpty() )
        {
            Long version = changeSetVersionService.getCurrentChangeSetVersion().getVersion();
            Long subVersion = exportStore.beginExport( version );
            do
            {
                if ( !exportStore.saveChangeSet( changeSet ) )
                {
                    logger.severe( "Problem by storing export data!" );
                    throw new BulkLoaderException( "Problem by storing export data!" );
                }
                changeSet = dataModelDiff.getNextDiff();
            }
            while ( !changeSet.isEmpty() );
            exportStore.finishExport( version, subVersion );
        }
    }

    /**
     * Callback method for datastore
     */
    @Override
    public void entityOpDone( ChangeSetEntity entity )
    {
        // wrap the entity into ChangeSet
        ChangeSet cs = ChangeSet.createChangeSet().addEntity( entity );
        // send the change set to the progress diary
        progressDiary.writeEntry( cs );
    }
}
