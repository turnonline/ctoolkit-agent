package com.google.appengine.tools.mapreduce.inputs;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.mapreduce.InputReader;
import com.google.appengine.tools.mapreduce.impl.util.SplitUtil;
import org.ctoolkit.agent.service.impl.datastore.MetadataItemShardStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom datastore input for {@link org.ctoolkit.agent.model.BaseMetadata}.
 * It must be placed in this package because {@link BaseDatastoreInput} has package access only
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MetadataItemShardInput
        extends BaseDatastoreInput<Entity, DatastoreInputReader>
{
    private static final Logger logger = LoggerFactory.getLogger(MetadataItemShardInput.class);

    private int shardCount;

    private String kind;
    private String parentKey;

    public MetadataItemShardInput( String kind, String parentKey, int shardCount )
    {
        super( null, shardCount );

        this.kind = kind;
        this.parentKey = parentKey;
        this.shardCount = shardCount;
    }

    @Override
    protected DatastoreInputReader createReader( Query query )
    {
        return new DatastoreInputReader( query );
    }

    @Override
    public List<InputReader<Entity>> createReaders()
    {
        List<Query> subQueries = splitQuery();
        List<DatastoreInputReader> readers = new ArrayList<>( subQueries.size() );
        for ( Query query : subQueries )
        {
            logger.info( "Creating reader for sub query: " + query.toString() );
            readers.add( createReader( query ) );
        }

        List<InputReader<Entity>> result = new ArrayList<>();
        for ( List<DatastoreInputReader> readersForShard : SplitUtil.split( readers, shardCount, false ) )
        {
            result.add( new ConcatenatingInputReader<>( readersForShard ) );
        }
        return result;
    }

    private List<Query> splitQuery()
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MetadataItemShardStrategy shardStrategy = new MetadataItemShardStrategy( datastore );
        return shardStrategy.splitQuery( kind, parentKey );
    }
}
