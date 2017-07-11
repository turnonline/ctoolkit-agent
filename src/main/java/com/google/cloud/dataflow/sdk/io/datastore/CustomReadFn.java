package com.google.cloud.dataflow.sdk.io.datastore;

import com.google.datastore.v1.EntityResult;
import com.google.datastore.v1.Query;
import com.google.datastore.v1.QueryResultBatch;
import com.google.datastore.v1.RunQueryRequest;
import com.google.datastore.v1.RunQueryResponse;
import com.google.datastore.v1.client.Datastore;
import com.google.protobuf.Int32Value;

import javax.annotation.Nullable;

import static com.google.common.base.Verify.verify;
import static com.google.datastore.v1.QueryResultBatch.MoreResultsType.NOT_FINISHED;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class CustomReadFn
        extends DatastoreV1.Read.ReadFn
{
    static final int QUERY_BATCH_LIMIT = 500;

    private transient Datastore datastore;

    private String projectId;

    public CustomReadFn( String projectId )
    {
        super( DatastoreV1.Read.V1Options.from( projectId, Query.newBuilder().build(), null ) );
        this.projectId = projectId;
    }

    public void startBundle( Context c ) throws Exception
    {
        datastore = new DatastoreV1.V1DatastoreFactory().getDatastore( c.getPipelineOptions(), projectId );
    }

    /**
     * Read and output entities for the given query.
     */
    // TODO: override because original method throws NoSuchMethod for query.toBuilder().clone();
    @Override
    public void processElement( ProcessContext context ) throws Exception
    {
        Query query = context.element();
        String namespace = null;
        int userLimit = query.hasLimit()
                ? query.getLimit().getValue() : Integer.MAX_VALUE;

        boolean moreResults = true;
        QueryResultBatch currentBatch = null;

        while ( moreResults )
        {
            Query.Builder queryBuilder = query.toBuilder().clone();
            queryBuilder.setLimit( Int32Value.newBuilder().setValue(
                    Math.min( userLimit, QUERY_BATCH_LIMIT ) ) );

            if ( currentBatch != null && !currentBatch.getEndCursor().isEmpty() )
            {
                queryBuilder.setStartCursor( currentBatch.getEndCursor() );
            }

            RunQueryRequest request = makeRequest( queryBuilder.build(), namespace );
            RunQueryResponse response = datastore.runQuery( request );

            currentBatch = response.getBatch();

            // MORE_RESULTS_AFTER_LIMIT is not implemented yet:
            // https://groups.google.com/forum/#!topic/gcd-discuss/iNs6M1jA2Vw, so
            // use result count to determine if more results might exist.
            int numFetch = currentBatch.getEntityResultsCount();
            if ( query.hasLimit() )
            {
                verify( userLimit >= numFetch,
                        "Expected userLimit %s >= numFetch %s, because query limit %s must be <= userLimit",
                        userLimit, numFetch, query.getLimit() );
                userLimit -= numFetch;
            }

            // output all the entities from the current batch.
            for ( EntityResult entityResult : currentBatch.getEntityResultsList() )
            {
                context.output( entityResult.getEntity() );
            }

            // Check if we have more entities to be read.
            moreResults =
                    // User-limit does not exist (so userLimit == MAX_VALUE) and/or has not been satisfied
                    ( userLimit > 0 )
                            // All indications from the API are that there are/may be more results.
                            && ( ( numFetch == QUERY_BATCH_LIMIT )
                            || ( currentBatch.getMoreResults() == NOT_FINISHED ) );
        }
    }

    static RunQueryRequest makeRequest( Query query, @Nullable String namespace )
    {
        RunQueryRequest.Builder requestBuilder = RunQueryRequest.newBuilder().setQuery( query );
        if ( namespace != null )
        {
            requestBuilder.getPartitionIdBuilder().setNamespaceId( namespace );
        }
        return requestBuilder.build();
    }
}
