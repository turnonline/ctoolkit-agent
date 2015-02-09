package org.ctoolkit.agent.datastore.bigtable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The entity pool for batch processing of inserts and deletes.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DataStoreEntityPool
{
    private static final Logger logger = LoggerFactory.getLogger( DataStoreEntityPool.class );

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
