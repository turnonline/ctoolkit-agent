package org.ctoolkit.agent.datastore.bigtable;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.ChangeSetEntity;
import org.ctoolkit.agent.datastore.AbstractDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * The AppEngine implementation of the datastore.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class BigTableDataStore
        extends AbstractDataStore
{
    private static final Logger logger = LoggerFactory.getLogger( BigTableDataStore.class );

    /**
     * The datastore to work on
     */
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /**
     * The entity pool for bulk processing
     */
    private final DataStoreEntityPool entityPool;

    /**
     * The encoder converting change sets to data store entities vice versa
     */
    private final BigTableEntityEncoder encoder;

    private String kind;

    private Cursor cursor;

    /**
     * Default constructor
     */
    @Inject
    protected BigTableDataStore()
    {
        entityPool = new DataStoreEntityPool( datastore );
        encoder = new BigTableEntityEncoder();
    }

    @Override
    public void startUpdate()
    {
        logger.info( "Starting data store upgrading" );
    }

    @Override
    protected void clearEntityKind( String kind )
    {
        // cleaning table is the same as dropping it
        dropEntityKind( kind );
    }

    @Override
    protected void dropEntityKind( String kind )
    {
        logger.info( "Dropping kind '" + kind + "'" );

        int numItems = 0;
        // remove all entities with given kind
        while ( true )
        {
            Query query = new Query( kind ).setKeysOnly();
            PreparedQuery preparedQuery = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = preparedQuery.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    numItems++;
                    entityPool.delete( entity.getKey() );
                }
                entityPool.flush();
            }
            else
            {
                break;
            }
        }

        logger.info( numItems + " '" + kind + "' successfully removed" );
    }

    @Override
    protected void addEntityProperty( String kind, String property, String type, String defVal )
            throws AgentException
    {
        logger.info( "Updating kind '" + kind + "', adding property '" + property + "' with type "
                + type + " and with default value " + defVal );

        int numItems = changeEntityKindProperty( kind, property, type, defVal );

        logger.info( numItems + " '" + kind + "' successfully added" );
    }

    @Override
    protected void updateEntityPropertyValue( String kind, String property, String type, String defVal )
            throws AgentException
    {
        logger.info( "Updating kind '" + kind + "' property '" + property + "' to " + defVal );

        int numItems = changeEntityKindProperty( kind, property, type, defVal );

        logger.info( numItems + " '" + kind + "' successfully updated" );
    }

    @Override
    protected void removeEntityProperty( String kind, String property )
    {
        logger.info( "Removing kind '" + kind + "' property '" + property + "'" );

        int numItems = 0;

        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery pq = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = pq.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    entity.removeProperty( property );
                    entityPool.put( entity );
                    numItems++;
                }
            }
            else
            {
                break;
            }
        }
        entityPool.flush();

        logger.info( numItems + " '" + kind + "' successfully updated" );
    }

    /**
     * Sets property of the entity to the given type and value.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be set
     * @param type     the type of the property to be set
     * @param value    the value of the property to be set
     * @return the number of modified entities
     * @throws AgentException
     */
    private int changeEntityKindProperty( String kind, String property, String type, String value )
            throws AgentException
    {
        int entitiesChanged = 0;

        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery prepQuery = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = prepQuery.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    entity.setProperty( property, encoder.encodeProperty( type, value ) );
                    entityPool.put( entity );
                    entitiesChanged++;
                }
            }
            else
            {
                break;
            }
        }
        entityPool.flush();

        return entitiesChanged;
    }

    @Override
    protected void removeEntity( String key )
    {
        logger.info( "Deleting entity from datastore: " + key );

        entityPool.delete( KeyFactory.stringToKey( key ) );
        entityPool.flush();
    }

    @Override
    protected ChangeSetEntity addEntity( ChangeSetEntity csEntity ) throws AgentException
    {
        logger.info( "Putting entity into datastore: " + csEntity );

        Entity dsEntity = encoder.toDataStoreEntity( csEntity );
        Key key = datastore.put( dsEntity );
        ChangeSetEntity changeSetEntity = new ChangeSetEntity( csEntity, false /*do not need the properties*/ );
        changeSetEntity.setKey( KeyFactory.keyToString( key ) );

        return changeSetEntity;
    }

    @Override
    public void setFirstChangeSet( String kind, String cursor )
    {
        this.kind = kind;

        if ( cursor != null )
        {
            this.cursor = Cursor.fromWebSafeString( cursor );
        }
    }

    @Override
    public ChangeSet getNextChangeSet()
    {
        // create empty, default change set
        ChangeSet changeSet = ChangeSet.createChangeSet();

        if ( kind != null )
        {
            Query query = new Query( kind );
            // read the entities
            // TODO: implement cursor fetching and bulk reading - limit should be read from global config
            FetchOptions fetchOptions = FetchOptions.Builder.withDefaults().limit( 1 );
            if ( null != cursor )
            {
                fetchOptions.startCursor( cursor );
            }

            // get the data store
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            QueryResultList<Entity> result = datastoreService.prepare( query ).asQueryResultList( fetchOptions );
            if ( !result.isEmpty() )
            {
                for ( Entity entity : result )
                {
                    changeSet.addEntity( encoder.toChangeSetEntity( entity ) );
                }
                this.cursor = result.getCursor();
            }
            else
            {
                this.cursor = null;
            }
        }

        return changeSet;
    }

    @Override
    public String getCursor()
    {
        return cursor == null ? null : cursor.toWebSafeString();
    }

    @Override
    public void addToQueue( DeferredTask runnable )
    {
        TaskOptions options = TaskOptions.Builder.withDefaults();

        Queue queue = QueueFactory.getDefaultQueue();
        queue.add( options.payload( runnable ) );
    }

    @Override
    public void endUpdate()
    {
        logger.info( "Data store upgrading done" );
        entityPool.flush();
    }
}
