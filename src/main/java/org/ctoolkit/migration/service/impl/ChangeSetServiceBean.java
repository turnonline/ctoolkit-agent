package org.ctoolkit.migration.service.impl;

import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.NoSuchObjectException;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.common.base.Charsets;
import org.ctoolkit.migration.exception.ObjectNotFoundException;
import org.ctoolkit.migration.exception.ProcessAlreadyRunning;
import org.ctoolkit.migration.model.BaseMetadata;
import org.ctoolkit.migration.model.ChangeMetadata;
import org.ctoolkit.migration.model.ChangeSet;
import org.ctoolkit.migration.model.ChangeSetEntity;
import org.ctoolkit.migration.model.ChangeSetModelKindOp;
import org.ctoolkit.migration.model.ChangeSetModelKindPropOp;
import org.ctoolkit.migration.model.ExportMetadata;
import org.ctoolkit.migration.model.ImportMetadata;
import org.ctoolkit.migration.model.JobInfo;
import org.ctoolkit.migration.model.JobState;
import org.ctoolkit.migration.service.ChangeSetService;
import org.ctoolkit.migration.service.DataAccess;
import org.ctoolkit.migration.service.impl.datastore.EntityPool;
import org.ctoolkit.migration.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.migration.util.XmlUtils;

import javax.inject.Inject;

/**
 * Implementation of {@link ChangeSetService}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBean
        implements ChangeSetService
{
    private final EntityPool pool;

    private final DataAccess dataAccess;

    private final JobSpecificationFactory jobSpecificationFactory;

    private final MapReduceSettings mapReduceSettings;

    private final PipelineService pipelineService;

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
    }

    @Override
    public ImportMetadata createImportMetadata( ImportMetadata importMetadata )
    {
        importMetadata.save();
        return importMetadata;
    }

    @Override
    public ChangeMetadata createChangeMetadata( ChangeMetadata changeMetadata )
    {
        changeMetadata.save();
        return changeMetadata;
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
    public ChangeMetadata updateChangeMetadata( ChangeMetadata changeMetadata )
    {
        changeMetadata.save();
        return changeMetadata;
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
    public ChangeMetadata getChangeMetadata( String key )
    {
        return dataAccess.find( ChangeMetadata.class, key );
    }

    @Override
    public ExportMetadata getExportMetadata( String key )
    {
        return dataAccess.find( ExportMetadata.class, key );
    }

    @Override
    public void deleteImportMetadata( String key )
    {
        dataAccess.delete( ImportMetadata.class, key );
    }

    @Override
    public void deleteChangeMetadata( String key )
    {
        dataAccess.delete( ChangeMetadata.class, key );
    }

    @Override
    public void deleteExportMetadata( String key )
    {
        dataAccess.delete( ExportMetadata.class, key );
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
    public JobInfo getImportJobInfo( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        if ( importMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        return getJobInfoInternal(importMetadata, key);
    }

    @Override
    public JobInfo getChangeJobInfo( String key )
    {
        ChangeMetadata changeMetadata = getChangeMetadata( key );

        if ( changeMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        return getJobInfoInternal(changeMetadata, key);
    }

    @Override
    public JobInfo getExportJobInfo( String key )
    {
        ExportMetadata exportMetadata = getExportMetadata( key );

        if ( exportMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        return getJobInfoInternal(exportMetadata, key);
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
        if ( changeSet.hasModel() )
        {
            // process KindOps
            if ( changeSet.getModel().hasKindOps() )
            {
                for ( ChangeSetModelKindOp kindOp : changeSet.getModel().getKindOps() )
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
            if ( changeSet.getModel().hasKindPropOps() )
            {
                for ( ChangeSetModelKindPropOp kindPropOp : changeSet.getModel().getKindPropOps() )
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
    public byte[] exportChangeSet( String entity )
    {
        ChangeSet changeSet = dataAccess.exportChangeSet( entity );
        return XmlUtils.marshall( changeSet ).getBytes( Charsets.UTF_8 );
    }

    // -- private helpers


    private JobInfo getJobInfoInternal( BaseMetadata baseMetadata, String key) {
        try
        {
            com.google.appengine.tools.pipeline.JobInfo pipelineJobInfo = pipelineService.getJobInfo( baseMetadata.getMapReduceJobId() );
            JobInfo jobInfo = new JobInfo();
            jobInfo.setId( key );
            jobInfo.setMapReduceJobId( baseMetadata.getMapReduceJobId() );
            jobInfo.setState( JobState.valueOf( pipelineJobInfo.getJobState().name() ) );
            jobInfo.setStackTrace( pipelineJobInfo.getError() );
            jobInfo.setProcessedItems( baseMetadata.getProcessedItems() );
            jobInfo.setTotalItems( baseMetadata.getItemsCount() );

            return jobInfo;
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + baseMetadata.getMapReduceJobId(), e );
        }
    }
}
