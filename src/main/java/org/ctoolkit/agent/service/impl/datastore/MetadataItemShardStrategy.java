package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation maps every {@link org.ctoolkit.agent.model.BaseMetadataItem} key from {@link org.ctoolkit.agent.model.BaseMetadata#itemsRef} to its own shard
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MetadataItemShardStrategy
{
    private static final Logger logger = LoggerFactory.getLogger( MetadataItemShardStrategy.class );

    private final DatastoreService datastore;

    public MetadataItemShardStrategy( DatastoreService datastore )
    {
        this.datastore = datastore;
    }

    public List<Query> splitQuery( String kind, String parentKey )
    {
        return toQueries( kind, parentKey );
    }

    // -- private helpers

    private List<Query> toQueries( String kind, String parentKeyString )
    {
        Key parentKey = KeyFactory.stringToKey( parentKeyString );

        List<Key> keys = getKeys( parentKey );
        List<Query> result = new ArrayList<>( keys.size() );

        logger.info( "Retrieving keys [" + keys.size() + "]." );

        for ( Key key : keys )
        {
            logger.info( "Add key to query: " + key );

            Query subQuery = new Query( kind );
            subQuery.setFilter( new Query.FilterPredicate( Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key ) );
            result.add( subQuery );
        }

        return result;
    }

    @SuppressWarnings( "unchecked" )
    private List<Key> getKeys( Key parentKey )
    {
        try
        {
            Entity parent = datastore.get( parentKey );
            return ( List<Key> ) parent.getProperty( "itemsRef" );
        }
        catch ( EntityNotFoundException e )
        {
            throw new IllegalArgumentException( "Unable to load parent key: " + parentKey, e );
        }
    }
}
