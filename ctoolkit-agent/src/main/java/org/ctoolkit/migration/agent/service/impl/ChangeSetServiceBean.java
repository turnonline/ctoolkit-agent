package org.ctoolkit.migration.agent.service.impl;

import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.NoSuchObjectException;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.migration.agent.model.BaseMetadata;
import org.ctoolkit.migration.agent.model.BaseMetadataItem;
import org.ctoolkit.migration.agent.model.ChangeJobInfo;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ExportJobInfo;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.Filter;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.model.KindMetaData;
import org.ctoolkit.migration.agent.model.MetadataItemKey;
import org.ctoolkit.migration.agent.model.MetadataKey;
import org.ctoolkit.migration.agent.model.PropertyMetaData;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.service.DataAccess;
import org.ctoolkit.migration.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.migration.agent.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.migration.agent.service.impl.datastore.MapSpecificationProvider;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetEntity;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindOp;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindPropOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.appengine.tools.pipeline.JobInfo.State.RUNNING;

/**
 * Implementation of {@link ChangeSetService}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBean
        implements ChangeSetService
{
    private static final Logger log = LoggerFactory.getLogger( ChangeSetServiceBean.class );

    private final EntityPool pool;

    private final DataAccess dataAccess;

    private final JobSpecificationFactory jobSpecificationFactory;

    private final MapReduceSettings mapReduceSettings;

    private final PipelineService pipelineService;

    private Set<String> systemKinds = new HashSet<>();

    private Map<Class, Provider> jobInfoProviders = new HashMap<>();

    @Inject
    public ChangeSetServiceBean( EntityPool pool,
                                 DataAccess dataAccess,
                                 JobSpecificationFactory jobSpecificationFactory,
                                 MapReduceSettings mapReduceSettings,
                                 PipelineService pipelineService )
    {
        this.pool = pool;
        this.dataAccess = dataAccess;
        this.jobSpecificationFactory = jobSpecificationFactory;
        this.mapReduceSettings = mapReduceSettings;
        this.pipelineService = pipelineService;

        systemKinds.add( "MR-IncrementalTask" );
        systemKinds.add( "MR-ShardedJob" );
        systemKinds.add( "pipeline-barrier" );
        systemKinds.add( "pipeline-fanoutTask" );
        systemKinds.add( "pipeline-job" );
        systemKinds.add( "pipeline-jobInstanceRecord" );
        systemKinds.add( "pipeline-slot" );
        systemKinds.add( "pipeline-exception" );
        systemKinds.add( "__GsFileInfo__" );

        systemKinds.add( "_ImportMetadata" );
        systemKinds.add( "_ImportMetadataItem" );
        systemKinds.add( "_ExportMetadata" );
        systemKinds.add( "_ExportMetadataItem" );
        systemKinds.add( "_ChangeMetadata" );
        systemKinds.add( "_ChangeMetadataItem" );

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
    public <M extends BaseMetadata> M create( M metadata )
    {
        metadata.save();
        // TODO: store blob
        return metadata;
    }

    @Override
    public <M extends BaseMetadata> M update( M metadata )
    {
        // TODO: store blob
        return dataAccess.update( metadata );
    }

    @Override
    public <M extends BaseMetadata> M get( MetadataKey<M> key )
    {
        return dataAccess.find( key.getMetadataClass(), key.getKey() );
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( M metadata )
    {
        for ( BaseMetadataItem item : metadata.getItems() )
        {
            // TODO: delete blob
            dataAccess.delete( item.getClass(), item.getKey() );
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
    public <M extends BaseMetadata> List<M> list( Filter<M> filter )
    {
        return dataAccess.find( filter.getMetadataClass(), filter );
    }

    // ------------------------------------------
    // -- metadata item
    // ------------------------------------------

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI create( MI metadataItem )
    {
        M importMetadata = metadataItem.getMetadata();
        importMetadata.getItems().add( metadataItem );
        importMetadata.save();
        // TODO: store blob
        return metadataItem;
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI update( MI metadataItem )
    {
        // TODO: store blob
        return dataAccess.update( metadataItem );
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI get( MetadataItemKey<MI> key )
    {
        // TODO: load blob
        return dataAccess.find( key.getMetadataItemClass(), key.getKey() );
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( MI metadataItem )
    {
        M importMetadata = metadataItem.getMetadata();
        importMetadata.getItems().remove( importMetadata );
        importMetadata.save();

        // TODO: remove blob
    }

    // ------------------------------------------
    // -- job
    // ------------------------------------------

    @Override
    public <M extends BaseMetadata> void startJob( M metadata ) throws ProcessAlreadyRunning
    {
        // check if mapReduceJob is running
        if ( metadata.getMapReduceJobId() != null )
        {
            try
            {
                com.google.appengine.tools.pipeline.JobInfo jobInfo = pipelineService.getJobInfo( metadata.getMapReduceJobId() );
                if ( jobInfo.getJobState() == RUNNING )
                {
                    throw new ProcessAlreadyRunning( "ImportJob process is already running: " + metadata.getMapReduceJobId() );
                }
            }
            catch ( NoSuchObjectException e )
            {
                // silently ignore
            }
        }

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

}
