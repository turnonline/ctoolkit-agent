package org.ctoolkit.migration.agent.service.impl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.NoSuchObjectException;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.googlecode.objectify.VoidWork;
import org.ctoolkit.api.migration.CtoolkitAgent;
import org.ctoolkit.api.migration.model.ImportBatch;
import org.ctoolkit.migration.agent.config.CtoolkitAgentFactory;
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.migration.agent.model.AuditFilter;
import org.ctoolkit.migration.agent.model.BaseMetadata;
import org.ctoolkit.migration.agent.model.BaseMetadataFilter;
import org.ctoolkit.migration.agent.model.BaseMetadataItem;
import org.ctoolkit.migration.agent.model.ChangeJobInfo;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.CtoolkitAgentConfiguration;
import org.ctoolkit.migration.agent.model.ExportJobInfo;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.model.KindMetaData;
import org.ctoolkit.migration.agent.model.MetadataAudit;
import org.ctoolkit.migration.agent.model.MetadataAudit.Action;
import org.ctoolkit.migration.agent.model.MetadataItemKey;
import org.ctoolkit.migration.agent.model.MetadataKey;
import org.ctoolkit.migration.agent.model.MigrationJobConfiguration;
import org.ctoolkit.migration.agent.model.PropertyMetaData;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.service.DataAccess;
import org.ctoolkit.migration.agent.service.RestContext;
import org.ctoolkit.migration.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.migration.agent.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.migration.agent.service.impl.datastore.MapSpecificationProvider;
import org.ctoolkit.migration.agent.service.impl.event.Auditable;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetEntity;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindOp;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindPropOp;
import org.ctoolkit.services.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.appengine.tools.pipeline.JobInfo.State.RUNNING;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.ctoolkit.migration.agent.config.AgentModule.BUCKET_NAME;

/**
 * Implementation of {@link ChangeSetService}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBean
        implements ChangeSetService
{
    private static final Logger log = LoggerFactory.getLogger( ChangeSetServiceBean.class );

    private final EntityPool pool;

    private final DataAccess dataAccess;

    private final StorageService storageService;

    private final Provider<RestContext> restContext;

    private final JobSpecificationFactory jobSpecificationFactory;

    private final CtoolkitAgentFactory ctoolkitAgentFactory;

    private final MapReduceSettings mapReduceSettings;

    private final PipelineService pipelineService;

    private final Set<String> systemKinds = new HashSet<>();

    private final Map<Class, Provider> jobInfoProviders = new HashMap<>();

    private final String bucketName;

    @Inject
    public ChangeSetServiceBean( EntityPool pool,
                                 DataAccess dataAccess,
                                 StorageService storageService,
                                 Provider<RestContext> restContext,
                                 JobSpecificationFactory jobSpecificationFactory,
                                 CtoolkitAgentFactory ctoolkitAgentFactory,
                                 MapReduceSettings mapReduceSettings,
                                 PipelineService pipelineService,
                                 @Named( BUCKET_NAME ) String bucketName )
    {
        this.pool = pool;
        this.dataAccess = dataAccess;
        this.storageService = storageService;
        this.restContext = restContext;
        this.jobSpecificationFactory = jobSpecificationFactory;
        this.ctoolkitAgentFactory = ctoolkitAgentFactory;
        this.mapReduceSettings = mapReduceSettings;
        this.pipelineService = pipelineService;
        this.bucketName = bucketName;

        systemKinds.add( "MR-IncrementalTask" );
        systemKinds.add( "MR-ShardedJob" );
        systemKinds.add( "MR-ShardRetryState" );
        systemKinds.add( "pipeline-barrier" );
        systemKinds.add( "pipeline-fanoutTask" );
        systemKinds.add( "pipeline-job" );
        systemKinds.add( "pipeline-jobInstanceRecord" );
        systemKinds.add( "pipeline-slot" );
        systemKinds.add( "pipeline-exception" );
        systemKinds.add( "__GsFileInfo__" );
        systemKinds.add( "_ah_FakeCloudStorage__app_default_bucket" );

        systemKinds.add( "_ImportMetadata" );
        systemKinds.add( "_ImportMetadataItem" );
        systemKinds.add( "_ExportMetadata" );
        systemKinds.add( "_ExportMetadataItem" );
        systemKinds.add( "_ChangeMetadata" );
        systemKinds.add( "_ChangeMetadataItem" );
        systemKinds.add( "_MetadataAudit" );

        // init job info providers
        jobInfoProviders.put( ImportMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ImportJobInfo();
            }
        } );
        jobInfoProviders.put( ExportMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ExportJobInfo();
            }
        } );
        jobInfoProviders.put( ChangeMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ChangeJobInfo();
            }
        } );
    }

    // ------------------------------------------
    // -- metadata
    // ------------------------------------------

    @Override
    @Auditable( action = Action.CREATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M create( M metadata )
    {
        // create metadata
        metadata.save();

        // create blob
        for ( MI item : metadata.getItems() )
        {
            if ( item.getData() != null )
            {
                storageService.store(
                        item.getData(),
                        item.getDataType().mimeType(),
                        item.newFileName(),
                        bucketName
                );
            }
        }

        // update fileName
        metadata.save();

        return metadata;
    }

    @Override
    @Auditable( action = Action.UPDATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M update( M metadata )
    {
        // update metadata
        metadata.save();

        // update blob
        for ( MI item : metadata.getItems() )
        {
            // remove orphans
            for ( MI orphan : metadata.getOrphans() )
            {
                if ( orphan.getFileName() != null )
                {
                    storageService.delete( orphan.getFileName(), bucketName );
                }
            }

            if ( item.getData() != null )
            {
                storageService.store(
                        item.getData(),
                        item.getDataType().mimeType(),
                        item.getFileName() != null ? item.getFileName() : item.newFileName(),
                        bucketName );
            }
        }

        // update fileName
        metadata.save();

        return metadata;
    }

    @Override
    public <M extends BaseMetadata> M get( MetadataKey<M> key )
    {
        return dataAccess.find( key.getMetadataClass(), key.getKey() );
    }

    @Override
    @Auditable( action = Action.DELETE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( M metadata )
    {
        for ( BaseMetadataItem item : metadata.getItems() )
        {
            // remove item
            dataAccess.delete( item.getClass(), item.getKey() );

            // remove data
            storageService.delete( item.getFileName() );
        }

        dataAccess.delete( metadata.getClass(), metadata.getKey() );

        // delete job
        try
        {
            deleteJob( metadata );
        }
        catch ( ObjectNotFoundException e )
        {
            // silently ignore
        }
    }

    @Override
    public <M extends BaseMetadata> List<M> list( BaseMetadataFilter<M> filter )
    {
        return dataAccess.find( filter );
    }

    @Override
    public ImportMetadata migrate( ExportMetadata exportMetadata ) throws IOException
    {
        // check job is not already running, remove previous job
        checkJobAndRemoveIfExists( exportMetadata.getMapReduceMigrationJobId() );

        // create naked import
        ImportMetadata importMetadata = new ImportMetadata();
        importMetadata.setName( "Export from '" + exportMetadata.getName() + "'" );

        // call remote agent and create import
        ImportBatch importBatch = new ImportBatch();
        importBatch.setName( importMetadata.getName() );

        CtoolkitAgentConfiguration ctoolkitAgentConfiguration = new CtoolkitAgentConfiguration( restContext.get().getOnBehalfOfAgentUrl(), restContext.get().getGtoken() );
        CtoolkitAgent ctoolkitAgent = ctoolkitAgentFactory.provideCtoolkitAgent( ctoolkitAgentConfiguration ).get();
        importBatch = ctoolkitAgent.importBatch().insert( importBatch ).execute();
        importMetadata.setId( KeyFactory.stringToKey( importBatch.getKey() ).getId() );

        // start migrate job
        MigrationJobConfiguration jobConfiguration = new MigrationJobConfiguration( exportMetadata.getKey(), importBatch.getKey() );

        MapSpecificationProvider mapSpecificationProvider = jobSpecificationFactory
                .createMigrateJobSpecification( jobConfiguration, ctoolkitAgentConfiguration );
        String id = MapJob.start( mapSpecificationProvider.get(), mapReduceSettings );

        exportMetadata.setMapReduceMigrationJobId( id );
        exportMetadata.clearJobContext();
        exportMetadata.putToJobContext( "gtoken", ctoolkitAgentConfiguration.getGtoken() );
        exportMetadata.putToJobContext( "rootUrl", ctoolkitAgentConfiguration.getRootUrl() );
        exportMetadata.putToJobContext( "importKey", importBatch.getKey() );
        exportMetadata.save();

        return importMetadata;
    }

    // ------------------------------------------
    // -- metadata item
    // ------------------------------------------

    @Override
    @Auditable( action = Action.CREATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI create( final M metadata, final MI metadataItem )
    {
        // store again to persist file name
        dataAccess.create( metadataItem );

        final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        // update parent - in transaction because multiple threads can access parent
        ofy().transactNew( 5, new VoidWork()
        {
            @Override
            public void vrun()
            {
                Key parentKey = KeyFactory.stringToKey( metadata.getKey() );

                try
                {
                    Entity metadataEntity = datastoreService.get( parentKey );
                    List<Key> items = ( List<Key> ) metadataEntity.getProperty( "itemsRef" );
                    if ( items == null )
                    {
                        items = new ArrayList<>();
                    }
                    Key itemKey = KeyFactory.stringToKey( metadataItem.getKey() );
                    if ( !items.contains( itemKey ) )
                    {
                        items.add( itemKey );
                    }

                    metadataEntity.setUnindexedProperty( "itemsCount", items.size() );
                    metadataEntity.setUnindexedProperty( "itemsRef", items );
                    datastoreService.put( ofy().getTransaction(), metadataEntity );
                }
                catch ( EntityNotFoundException e )
                {
                    throw new RuntimeException( "Parent for item not found: " + parentKey, e );
                }
            }
        } );

        if ( metadataItem.getData() != null )
        {
            storageService.store(
                    metadataItem.getData(),
                    metadataItem.getDataType().mimeType(),
                    metadataItem.newFileName(),
                    bucketName
            );
        }

        // store again to persist file name
        dataAccess.update( metadataItem );

        return metadataItem;
    }

    @Override
    @Auditable( action = Action.UPDATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI update( MI metadataItem )
    {
        metadataItem = dataAccess.update( metadataItem );

        if ( metadataItem.getData() != null )
        {
            storageService.store(
                    metadataItem.getData(),
                    metadataItem.getDataType().mimeType(),
                    metadataItem.getFileName() != null ? metadataItem.getFileName() : metadataItem.newFileName(),
                    bucketName
            );
        }

        return metadataItem;
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI get( MetadataItemKey<MI> key )
    {
        MI item = dataAccess.find( key.getMetadataItemClass(), key.getKey() );
        if ( item.getFileName() != null )
        {
            item.setData( storageService.serve( item.getFileName(), bucketName ) );
        }

        return item;
    }

    @Override
    @Auditable( action = Action.DELETE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( final MI metadataItem )
    {
        if ( metadataItem.getFileName() != null )
        {
            storageService.delete( metadataItem.getFileName(), bucketName );
        }

        ofy().transactNew( 5, new VoidWork()
        {

            @Override
            public void vrun()
            {
                M importMetadata = metadataItem.getMetadata();
                importMetadata.getItems().remove( metadataItem );
                importMetadata.save();
            }
        } );
    }

    // ------------------------------------------
    // -- job
    // ------------------------------------------

    @Override
    @Auditable( action = Action.START_JOB )
    public <M extends BaseMetadata> void startJob( M metadata ) throws ProcessAlreadyRunning
    {
        // check if mapReduceJob is running
        checkJobAndRemoveIfExists( metadata.getMapReduceJobId() );

        MapSpecificationProvider mapSpecificationProvider;

        if ( metadata.getClass() == ImportMetadata.class )
        {
            mapSpecificationProvider = jobSpecificationFactory.createImportJobSpecification( metadata.getKey() );
        }
        else if ( metadata.getClass() == ExportMetadata.class )
        {
            mapSpecificationProvider = jobSpecificationFactory.createExportJobSpecification( metadata.getKey() );
        }
        else if ( metadata.getClass() == ChangeMetadata.class )
        {
            mapSpecificationProvider = jobSpecificationFactory.createChangeJobSpecification( metadata.getKey() );
        }
        else
        {
            throw new IllegalArgumentException( "Unknown metadata type: " + metadata.getClass() );
        }


        String id = MapJob.start( mapSpecificationProvider.get(), mapReduceSettings );
        metadata.setMapReduceJobId( id );
        metadata.reset();
        metadata.save();
    }

    @Override
    @Auditable( action = Action.DELETE_JOB )
    public <M extends BaseMetadata> void deleteJob( M metadata )
    {
        if ( metadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for: " + metadata );
        }

        try
        {
            pipelineService.deletePipelineRecords( metadata.getMapReduceJobId(), true, false );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + metadata.getMapReduceJobId(), e );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <JI extends JobInfo, M extends BaseMetadata> JI getJobInfo( M metadata )
    {
        com.google.appengine.tools.pipeline.JobInfo pipelineJobInfo = null;

        if ( metadata.getMapReduceJobId() != null )
        {
            try
            {
                pipelineJobInfo = pipelineService.getJobInfo( metadata.getMapReduceJobId() );
            }
            catch ( NoSuchObjectException e )
            {
                log.error( "Map reduce job not found for key: " + metadata.getMapReduceJobId(), e );
            }
        }

        JI jobInfo = ( JI ) jobInfoProviders.get( metadata.getClass() ).get();
        jobInfo.setId( metadata.getKey() );
        jobInfo.setMapReduceJobId( metadata.getMapReduceJobId() );
        jobInfo.setProcessedItems( metadata.getProcessedItems() );
        jobInfo.setProcessedErrorItems( metadata.getProcessedErrorItems() );
        jobInfo.setTotalItems( metadata.getItemsCount() );

        if ( pipelineJobInfo != null )
        {
            jobInfo.setState( JobState.valueOf( pipelineJobInfo.getJobState().name() ) );
            jobInfo.setStackTrace( pipelineJobInfo.getError() );
        }

        return jobInfo;
    }

    @Override
    @Auditable( action = Action.CANCEL_JOB )
    public <M extends BaseMetadata> void cancelJob( M metadata )
    {
        if ( metadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for: " + metadata );
        }

        try
        {
            pipelineService.cancelPipeline( metadata.getMapReduceJobId() );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for id: " + metadata.getMapReduceJobId(), e );
        }
    }

    // ------------------------------------------
    // -- changesets
    // ------------------------------------------

    @Override
    public void importChangeSet( ChangeSet changeSet )
    {
        changeChangeSet( changeSet );
    }

    @Override
    public void changeChangeSet( ChangeSet changeSet )
    {
        pool.flush();

        // apply model changes
        if ( changeSet.hasModelObject() )
        {
            // process KindOps
            if ( changeSet.getModel().hasKindOpsObject() )
            {
                for ( ChangeSetModelKindOp kindOp : changeSet.getModel().getKindOp() )
                {
                    switch ( kindOp.getOp() )
                    {
                        case ChangeSetModelKindOp.OP_DROP:
                        {
                            dataAccess.dropEntity( kindOp.getKind() );
                            break;
                        }
                        case ChangeSetModelKindOp.OP_CLEAN:
                        {
                            dataAccess.clearEntity( kindOp.getKind() );
                            break;
                        }
                        default:
                        {
                            throw new IllegalArgumentException( "Unsupported Kind operation! " + kindOp.getOp() );
                        }
                    }
                }

                pool.flush();
            }

            // process KindPropOps
            if ( changeSet.getModel().hasKindPropOpsObject() )
            {
                for ( ChangeSetModelKindPropOp kindPropOp : changeSet.getModel().getKindPropsOp() )
                {
                    switch ( kindPropOp.getOp() )
                    {
                        case ChangeSetModelKindPropOp.OP_ADD:
                        {
                            dataAccess.addEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getNewName(),
                                    kindPropOp.getNewType(),
                                    kindPropOp.getNewValue()
                            );
                            break;
                        }
                        case ChangeSetModelKindPropOp.OP_REMOVE:
                        {
                            dataAccess.removeEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getProperty()
                            );
                            break;
                        }
                        case ChangeSetModelKindPropOp.OP_CHANGE:
                        {
                            dataAccess.changeEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getProperty(),
                                    kindPropOp.getNewName(),
                                    kindPropOp.getNewType(),
                                    kindPropOp.getNewValue()
                            );
                            break;
                        }
                        default:
                        {
                            throw new IllegalArgumentException( "Unsupported Kind prop operation! " + kindPropOp.getOp() );
                        }
                    }
                }

                // TODO: flush
            }
        }

        // apply entity changes
        if ( changeSet.hasEntities() )
        {
            for ( ChangeSetEntity cse : changeSet.getEntities().getEntity() )
            {
                dataAccess.addEntity( cse );
            }

            pool.flush();
        }
    }

    // ------------------------------------------
    // -- meta info
    // ------------------------------------------

    @Override
    public ChangeSet exportChangeSet( String entity )
    {
        return dataAccess.exportChangeSet( entity );
    }

    // ------------------------------------------
    // -- audits
    // ------------------------------------------

    @Override
    public List<MetadataAudit> list( AuditFilter filter )
    {
        return dataAccess.find( filter );
    }

    // ------------------------------------------
    // -- meta infos
    // ------------------------------------------

    @Override
    public List<KindMetaData> kinds()
    {
        List<KindMetaData> kinds = dataAccess.kinds();
        Iterable<KindMetaData> result = Iterables.filter( kinds, new Predicate<KindMetaData>()
        {
            @Override
            public boolean apply( @Nullable KindMetaData input )
            {
                return !systemKinds.contains( input.getKind() );
            }
        } );

        return Lists.newArrayList( result );
    }

    @Override
    public List<PropertyMetaData> properties( String kind )
    {
        return dataAccess.properties( kind );
    }

    // -- private helpers
    private void checkJobAndRemoveIfExists( String jobId )
    {
        if ( jobId != null )
        {
            try
            {
                com.google.appengine.tools.pipeline.JobInfo jobInfo = pipelineService.getJobInfo( jobId );
                if ( jobInfo.getJobState() == RUNNING )
                {
                    throw new ProcessAlreadyRunning( "ImportJob process is already running: " + jobId );
                }
                else
                {
                    // remove previous job
                    pipelineService.deletePipelineRecords( jobId, true, false );
                }
            }
            catch ( NoSuchObjectException e )
            {
                // silently ignore
            }
        }
    }

}
