package com.google.cloud.dataflow.sdk.io.datastore;

import com.google.datastore.v1.Query;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class CustomSplitQuery
        extends DatastoreV1.Read.SplitQueryFn
{
    public CustomSplitQuery( String projectId )
    {
        super( DatastoreV1.Read.V1Options.from( projectId, Query.newBuilder().build(), null ), 0 );
    }
}
