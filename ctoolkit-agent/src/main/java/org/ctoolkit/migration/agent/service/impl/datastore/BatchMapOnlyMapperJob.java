package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.inject.Injector;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.services.storage.StorageService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ConcurrentModificationException;

import static org.ctoolkit.migration.agent.config.AgentModule.BUCKET_NAME;

/**
 * Base mapper job for batch based jobs
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class BatchMapOnlyMapperJob
        extends MapOnlyMapper<Entity, Entity>
{
    @Inject
    protected static Injector injector;

    @Inject
    protected transient ChangeSetService changeSetService;

    @Inject
    protected transient DatastoreService datastoreService;

    @Inject
    protected transient StorageService storageService;

    @Inject
    @Named( BUCKET_NAME )
    protected transient String bucketName;

    @Override
    public void map( Entity value )
    {
        injector.injectMembers( this );
    }

    protected void updateParent( Entity item, JobState jobState )
    {
        int retries = 5;
        while ( true )
        {
            Transaction txn = datastoreService.beginTransaction();

            try
            {
                Entity parent = datastoreService.get( item.getParent() );

                if ( jobState == JobState.COMPLETED_SUCCESSFULLY )
                {
                    parent.setUnindexedProperty( "processedOk", ( ( Long ) parent.getProperty( "processedOk" ) ) + 1 );
                }
                if ( jobState == JobState.STOPPED_BY_ERROR )
                {
                    parent.setUnindexedProperty( "processedError", ( ( Long ) parent.getProperty( "processedError" ) ) + 1 );
                }

                datastoreService.put( parent );
                txn.commit();
                break;
            }
            catch ( EntityNotFoundException e )
            {
                throw new RuntimeException( "Parent not found for import item: " + item );
            }
            catch ( ConcurrentModificationException e )
            {
                if ( retries == 0 )
                {
                    throw e;
                }

                // Allow retry to occur
                --retries;
            }
            finally
            {
                if ( txn.isActive() )
                {
                    txn.rollback();
                }
            }
        }
    }
}
