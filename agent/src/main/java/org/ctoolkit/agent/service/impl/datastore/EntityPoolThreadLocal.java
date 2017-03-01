package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * The entity pool for batch processing of inserts and deletes for GAE datastore
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityPoolThreadLocal
        implements EntityPool
{
    private static final Logger logger = LoggerFactory.getLogger( EntityPoolThreadLocal.class );

    /**
     * The default number of entities to put.delete from the data store
     */
    private static final int DEFAULT_COUNT_LIMIT = 100;

    /**
     * The datastore service to work on
     */
    private final DatastoreService ds;

    /**
     * The max items to put into entity lists before flushing them into data store
     */
    private final int maxItems;

    /**
     * The list of holding entities waiting for putting them into data store
     */
    private final List<Entity> toPut = new ArrayList<>();

    /**
     * The list of holding entities waiting for deleting them from data store
     */
    private final List<Key> toDelete = new ArrayList<>();

    /**
     * The default constructor
     *
     * @param dataStore the AppEngine datastore service
     */
    @Inject
    public EntityPoolThreadLocal( DatastoreService dataStore )
    {
        this( dataStore, DEFAULT_COUNT_LIMIT );
    }

    private EntityPoolThreadLocal( DatastoreService ds, int maxItems )
    {
        logger.info( "Building entity pool for " + maxItems + " entries" );
        this.ds = ds;
        this.maxItems = maxItems;
    }

    public void put( Entity ent )
    {
        logger.info( "Adding entity into the put-pool" );
        if ( toPut.size() >= maxItems )
        {
            flushPuts();
        }
        toPut.add( ent );
    }

    public void delete( Key key )
    {
        if ( toDelete.size() >= maxItems )
        {
            flushDeletes();
        }
        toDelete.add( key );
    }

    public void flush()
    {
        try
        {
            if ( !toDelete.isEmpty() )
            {
                flushDeletes();
            }
            if ( !toPut.isEmpty() )
            {
                flushPuts();
            }
        }
        catch ( ConcurrentModificationException e )
        {
            logger.info( "::PUT POOL (" + toPut.size() + " items)" );
            logger.info( "::DELETE POOL (" + toDelete.size() + " items)" );
            throw e;
        }
    }

    private void flushPuts()
    {
        logger.info( "Flushing the put-pool (" + toPut.size() + " items)" );
        ds.put( toPut );
        toPut.clear();
    }

    private void flushDeletes()
    {
        logger.info( "Flushing the delete-pool (" + toDelete.size() + " items)" );
        ds.delete( toDelete );
        toDelete.clear();
    }

}
