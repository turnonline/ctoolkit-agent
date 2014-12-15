package org.ctoolkit.bulkloader.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Entity pool for batch processing inserts/deletes
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class DataStoreEntityPool
{

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger( DataStoreEntityPool.class.getName() );

    /**
     * Default number of entities to put.delete from the data store
     */
    private static final int DEFAULT_COUNT_LIMIT = 100;

    /**
     * Data store service to work on
     */
    private final DatastoreService ds;

    /**
     * Max items to put into entity lists before flushing them into data store
     */
    private final int maxItems;

    /**
     * List holding entities waiting for putting them into data store
     */
    private final List<Entity> toPut = new ArrayList<Entity>();

    /**
     * List holding entities waiting for deleting them from data store
     */
    private final List<Key> toDelete = new ArrayList<Key>();

    /**
     * Default constructor
     *
     * @param dataStore
     */
    public DataStoreEntityPool( DatastoreService dataStore )
    {
        this( dataStore, DEFAULT_COUNT_LIMIT );
    }

    public DataStoreEntityPool( DatastoreService ds, int maxItems )
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

    private void flushPuts()
    {
        logger.info( "Flushing the put-pool (" + toPut.size() + " items)" );
        ds.put( toPut );
        toPut.clear();
    }

    public void delete( Key key )
    {
        if ( toDelete.size() >= maxItems )
        {
            flushDeletes();
        }
        toDelete.add( key );
    }

    private void flushDeletes()
    {
        logger.info( "Flushing the delete-pool (" + toDelete.size() + " items)" );
        ds.delete( toDelete );
        toDelete.clear();
    }

    public void flush()
    {
        if ( !toPut.isEmpty() )
        {
            flushPuts();
        }
        if ( !toDelete.isEmpty() )
        {
            flushDeletes();
        }
    }
}
