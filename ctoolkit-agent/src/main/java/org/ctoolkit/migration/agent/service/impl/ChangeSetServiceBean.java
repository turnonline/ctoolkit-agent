package org.ctoolkit.migration.agent.service.impl;

import com.google.appengine.api.channel.ChannelService;
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
import org.ctoolkit.migration.agent.model.ChangeJobInfo;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;
import org.ctoolkit.migration.agent.model.ExportJobInfo;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.ExportMetadataItem;
import org.ctoolkit.migration.agent.model.Filter;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.model.KindMetaData;
import org.ctoolkit.migration.agent.model.PropertyMetaData;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.service.DataAccess;
import org.ctoolkit.migration.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.migration.agent.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetEntity;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindOp;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetModelKindPropOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final ChannelService channelService;

    private Set<String> systemKinds = new HashSet<>();

    @Inject
    public ChangeSetServiceBean( EntityPool pool,
                                 DataAccess dataAccess,
                                 JobSpecificationFactory jobSpecificationFactory,
                                 MapReduceSettings mapReduceSettings,
                                 PipelineService pipelineService,
                                 ChannelService channelService )
    {
        this.pool = pool;
        this.dataAccess = dataAccess;
        this.jobSpecificationFactory = jobSpecificationFactory;
        this.mapReduceSettings = mapReduceSettings;
        this.pipelineService = pipelineService;
        this.channelService = channelService;

        systemKinds.add( "MR-IncrementalTask" );
        systemKinds.add( "MR-SharedJob" );
        systemKinds.add( "pipeline-barrier" );
        systemKinds.add( "pipeline-fanoutTask" );
        systemKinds.add( "pipeline-job" );
        systemKinds.add( "pipeline-jobInstanceRecord" );
        systemKinds.add( "pipeline-slot" );
        systemKinds.add( "__GsFileInfo__" );

        systemKinds.add( "_ImportMetadata" );
        systemKinds.add( "_ImportMetadataItem" );
        systemKinds.add( "_ExportMetadata" );
        systemKinds.add( "_ExportMetadataItem" );
        systemKinds.add( "_ChangeMetadata" );
        systemKinds.add( "_ChangeMetadataItem" );
    }

    @Override
    public ImportMetadata createImportMetadata( ImportMetadata importMetadata )
    {
        importMetadata.save();
        return importMetadata;
    }

    @Override
    public ImportMetadataItem createImportMetadataItem( ImportMetadataItem importMetadataItem )
    {
        ImportMetadata importMetadata = importMetadataItem.getMetadata();
        importMetadata.getItems().add( importMetadataItem );
        importMetadata.save();

        return importMetadataItem;
    }

    @Override
    public ChangeMetadata createChangeMetadata( ChangeMetadata changeMetadata )
    {
        changeMetadata.save();
        return changeMetadata;
    }

    @Override
    public ChangeMetadataItem createChangeMetadataItem( ChangeMetadataItem changeMetadataItem )
    {
        ChangeMetadata changeMetadata = changeMetadataItem.getMetadata();
        changeMetadata.getItems().add( changeMetadataItem );
        changeMetadata.save();

        return changeMetadataItem;
    }

    @Override
    public ExportMetadata createExportMetadata( ExportMetadata exportMetadata )
    {
        exportMetadata.save();
        return exportMetadata;
    }

    @Override
    public ImportMetadata updateImportMetadata( ImportMetadata importMetadata )
    {
        importMetadata.save();
        return importMetadata;
    }

    @Override
    public ImportMetadataItem updateImportMetadataItem( ImportMetadataItem importMetadataItem )
    {
        return dataAccess.update( importMetadataItem );
    }

    @Override
    public ChangeMetadata updateChangeMetadata( ChangeMetadata changeMetadata )
    {
        changeMetadata.save();
        return changeMetadata;
    }

    @Override
    public ChangeMetadataItem updateChangeMetadataItem( ChangeMetadataItem changeMetadataItem )
    {
        return dataAccess.update( changeMetadataItem );
    }

    @Override
    public ExportMetadata updateExportMetadata( ExportMetadata exportMetadata )
    {
        exportMetadata.save();
        return exportMetadata;
    }

    @Override
    public ImportMetadata getImportMetadata( String key )
    {
        return dataAccess.find( ImportMetadata.class, key );
    }

    @Override
    public ImportMetadataItem getImportMetadataItem( String key )
    {
        return dataAccess.find( ImportMetadataItem.class, key );
    }

    @Override
    public ChangeMetadata getChangeMetadata( String key )
    {
        return dataAccess.find( ChangeMetadata.class, key );
    }

    @Override
    public ChangeMetadataItem getChangeMetadataItem( String key )
    {
        return dataAccess.find( ChangeMetadataItem.class, key );
    }

    @Override
    public ExportMetadata getExportMetadata( String key )
    {
        return dataAccess.find( ExportMetadata.class, key );
    }

    @Override
    public List<ImportMetadata> getImportMetadataList( Filter filter )
    {
        return dataAccess.find( ImportMetadata.class, filter );
    }

    @Override
    public List<ChangeMetadata> getChangeMetadataList( Filter filter )
    {
        return dataAccess.find( ChangeMetadata.class, filter );
    }

    @Override
    public List<ExportMetadata> getExportMetadataList( Filter filter )
    {
        return dataAccess.find( ExportMetadata.class, filter );
    }

    @Override
    public void deleteImportMetadata( ImportMetadata importMetadata )
    {
        for ( ImportMetadataItem item : importMetadata.getItems() )
        {
            dataAccess.delete( item.getClass(), item.getKey() );
        }

        dataAccess.delete( ImportMetadata.class, importMetadata.getKey() );
    }

    @Override
    public void deleteImportMetadataItem( String key )
    {
        ImportMetadataItem importMetadataItem = dataAccess.find( ImportMetadataItem.class, key );
        ImportMetadata importMetadata = importMetadataItem.getMetadata();
        importMetadata.getItems().remove( importMetadataItem );
        importMetadata.save();
    }

    @Override
    public void deleteChangeMetadata( ChangeMetadata changeMetadata )
    {
        for ( ChangeMetadataItem item : changeMetadata.getItems() )
        {
            dataAccess.delete( item.getClass(), item.getKey() );
        }

        dataAccess.delete( ChangeMetadata.class, changeMetadata.getKey() );
    }

    @Override
    public void deleteChangeMetadataItem( String key )
    {
        ChangeMetadataItem changeMetadataItem = dataAccess.find( ChangeMetadataItem.class, key );
        ChangeMetadata changeMetadata = changeMetadataItem.getMetadata();
        changeMetadata.getItems().remove( changeMetadataItem );
        changeMetadata.save();
    }

    @Override
    public void deleteExportMetadata( ExportMetadata exportMetadata )
    {
        for ( ExportMetadataItem item : exportMetadata.getItems() )
        {
            dataAccess.delete( item.getClass(), item.getKey() );
        }

        dataAccess.delete( ExportMetadata.class, exportMetadata.getKey() );
    }

    @Override
    public void startImportJob( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        // check if mapReduceJob is running
        if ( importMetadata.getMapReduceJobId() != null )
        {
            JobInfo previousJobInfo = getImportJobInfo( key );
            if ( previousJobInfo.getState() == JobState.RUNNING )
            {
                throw new ProcessAlreadyRunning( "ImportJob process is already running: " + importMetadata.getMapReduceJobId() );
            }
        }

        String id = MapJob.start( jobSpecificationFactory.createImportJobSpecification( key ).get(), mapReduceSettings );
        importMetadata.setMapReduceJobId( id );
        importMetadata.setToken( channelService.createChannel( key ) );
        importMetadata.reset();
        importMetadata.save();
    }

    @Override
    public void startChangeJob( String key )
    {
        ChangeMetadata changeMetadata = getChangeMetadata( key );

        // check if mapReduceJob is running
        if ( changeMetadata.getMapReduceJobId() != null )
        {
            JobInfo previousJobInfo = getChangeJobInfo( key );
            if ( previousJobInfo.getState() == JobState.RUNNING )
            {
                throw new ProcessAlreadyRunning( "ChangeJob process is already running: " + changeMetadata.getMapReduceJobId() );
            }
        }

        String id = MapJob.start( jobSpecificationFactory.createChangeJobSpecification( key ).get(), mapReduceSettings );
        changeMetadata.setMapReduceJobId( id );
        changeMetadata.setToken( channelService.createChannel( key ) );
        changeMetadata.reset();
        changeMetadata.save();
    }

    @Override
    public void startExportJob( String key )
    {
        ExportMetadata exportMetadata = getExportMetadata( key );

        // check if mapReduceJob is running
        if ( exportMetadata.getMapReduceJobId() != null )
        {
            JobInfo previousJobInfo = getExportJobInfo( key );
            if ( previousJobInfo.getState() == JobState.RUNNING )
            {
                throw new ProcessAlreadyRunning( "ExportJob process is already running: " + exportMetadata.getMapReduceJobId() );
            }
        }

        String id = MapJob.start( jobSpecificationFactory.createExportJobSpecification( key ).get(), mapReduceSettings );
        exportMetadata.setMapReduceJobId( id );
        exportMetadata.setToken( channelService.createChannel( key ) );
        exportMetadata.reset();
        exportMetadata.save();
    }

    @Override
    public void cancelImportJob( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        if ( importMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.cancelPipeline( importMetadata.getMapReduceJobId() );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for id: " + importMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void cancelChangeJob( String key )
    {
        ChangeMetadata changeMetadata = getChangeMetadata( key );

        if ( changeMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.cancelPipeline( changeMetadata.getMapReduceJobId() );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for id: " + changeMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void cancelExportJob( String key )
    {
        ExportMetadata exportMetadata = getExportMetadata( key );

        if ( exportMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.cancelPipeline( exportMetadata.getMapReduceJobId() );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for id: " + exportMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void deleteImportJob( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        if ( importMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.deletePipelineRecords( importMetadata.getMapReduceJobId(), true, false );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + importMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void deleteChangeJob( String key )
    {
        ChangeMetadata changeMetadata = getChangeMetadata( key );

        if ( changeMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.deletePipelineRecords( changeMetadata.getMapReduceJobId(), true, false );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + changeMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void deleteExportJob( String key )
    {
        ExportMetadata exportMetadata = getExportMetadata( key );

        if ( exportMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            pipelineService.deletePipelineRecords( exportMetadata.getMapReduceJobId(), true, false );
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + exportMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public ImportJobInfo getImportJobInfo( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        return getJobInfoInternal( importMetadata, new ImportJobInfo(), key );
    }

    @Override
    public ChangeJobInfo getChangeJobInfo( String key )
    {
        ChangeMetadata changeMetadata = getChangeMetadata( key );

        return getJobInfoInternal( changeMetadata, new ChangeJobInfo(), key );
    }

    @Override
    public ExportJobInfo getExportJobInfo( String key )
    {
        ExportMetadata exportMetadata = getExportMetadata( key );

        return getJobInfoInternal( exportMetadata, new ExportJobInfo(), key );
    }

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

    // -- private helpers

    private <T extends JobInfo> T getJobInfoInternal( BaseMetadata baseMetadata, T jobInfo, String key )
    {
        com.google.appengine.tools.pipeline.JobInfo pipelineJobInfo = null;

        if ( baseMetadata.getMapReduceJobId() != null )
        {
            try
            {
                pipelineJobInfo = pipelineService.getJobInfo( baseMetadata.getMapReduceJobId() );
            }
            catch ( NoSuchObjectException e )
            {
                log.error( "Map reduce job not found for key: " + baseMetadata.getMapReduceJobId(), e );
            }
        }

        jobInfo.setId( key );
        jobInfo.setMapReduceJobId( baseMetadata.getMapReduceJobId() );
        jobInfo.setProcessedItems( baseMetadata.getProcessedItems() );
        jobInfo.setTotalItems( baseMetadata.getItemsCount() );
        jobInfo.setToken( baseMetadata.getToken() );

        if ( pipelineJobInfo != null )
        {
            jobInfo.setState( JobState.valueOf( pipelineJobInfo.getJobState().name() ) );
            jobInfo.setStackTrace( pipelineJobInfo.getError() );
        }

        return jobInfo;
    }
}
