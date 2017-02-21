package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.inject.Injector;
import com.googlecode.objectify.VoidWork;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.services.storage.StorageService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.ctoolkit.migration.agent.config.AgentModule.BUCKET_NAME;

/**
 * Base mapper job for batch based jobs
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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

    protected void updateParent( final Entity item, final JobState jobState )
    {
        ofy().transactNew( 5, new VoidWork()
        {
            @Override
            public void vrun()
            {
                Parent parent = new Parent( item, jobState );
                ofy().save().entity( parent.get() ).now();
            }
        } );
    }

    private class Parent
    {
        private Entity parent;

        public Parent( final Entity item, final JobState jobState )
        {
            try
            {
                parent = datastoreService.get( item.getParent() );

                if ( jobState == JobState.COMPLETED_SUCCESSFULLY )
                {
                    updateProcessedProperty( item, parent, "processedOk" );
                }
                else
                {
                    updateProcessedProperty( item, parent, "processedError" );
                }
            }
            catch ( EntityNotFoundException e )
            {
                throw new RuntimeException( "Parent for item not found: " + item.getParent(), e );
            }
        }

        public Entity get()
        {
            return parent;
        }

        @SuppressWarnings( "unchecked" )
        private void updateProcessedProperty( Entity item, Entity parent, String property )
        {
            Long itemKey = item.getKey().getId();

            List<Long> processedProperty = ( List<Long> ) parent.getProperty( property );
            if ( processedProperty == null )
            {
                processedProperty = new ArrayList<>();
            }
            if ( !processedProperty.contains( itemKey ) )
            {
                processedProperty.add( itemKey );
            }

            parent.setUnindexedProperty( property, processedProperty );
        }
    }
}
