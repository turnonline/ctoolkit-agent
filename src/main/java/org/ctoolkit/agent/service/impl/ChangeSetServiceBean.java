package org.ctoolkit.agent.service.impl;

import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.NoSuchObjectException;
import com.google.appengine.tools.pipeline.PipelineService;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.agent.model.ChangeSet;
import org.ctoolkit.agent.model.ChangeSetEntity;
import org.ctoolkit.agent.model.ChangeSetModelKindOp;
import org.ctoolkit.agent.model.ChangeSetModelKindPropOp;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.JobSpecificationFactory;

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
    public ImportMetadata updateImportMetadata( ImportMetadata importMetadata )
    {
        importMetadata.save();
        return importMetadata;
    }

    @Override
    public ImportMetadata getImportMetadata( String key )
    {
        return dataAccess.find( ImportMetadata.class, key );
    }

    @Override
    public void deleteImportMetadata( String key )
    {
        dataAccess.delete( ImportMetadata.class, key );
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
                throw new ProcessAlreadyRunning( "ImportJob process is alredy running: " + importMetadata.getMapReduceJobId() );
            }
        }

        String id = MapJob.start( jobSpecificationFactory.createImportJobSpecification( key ).get(), mapReduceSettings );
        importMetadata.setMapReduceJobId( id );
        importMetadata.reset();
        importMetadata.save();
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
    public JobInfo getImportJobInfo( String key )
    {
        ImportMetadata importMetadata = getImportMetadata( key );

        if ( importMetadata.getMapReduceJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for key: " + key );
        }

        try
        {
            com.google.appengine.tools.pipeline.JobInfo pipelineJobInfo = pipelineService.getJobInfo( importMetadata.getMapReduceJobId() );
            JobInfo jobInfo = new JobInfo();
            jobInfo.setId( key );
            jobInfo.setMapReduceJobId( importMetadata.getMapReduceJobId() );
            jobInfo.setState( JobState.valueOf( pipelineJobInfo.getJobState().name() ) );
            jobInfo.setStackTrace( pipelineJobInfo.getError() );
            jobInfo.setProcessedItems( importMetadata.getProcessedItems() );
            jobInfo.setTotalItems( importMetadata.getItemsCount() );

            return jobInfo;
        }
        catch ( NoSuchObjectException e )
        {
            throw new ObjectNotFoundException( "Map reduce job not found for key: " + importMetadata.getMapReduceJobId(), e );
        }
    }

    @Override
    public void importChangeSet( ChangeSet changeSet )
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
}
