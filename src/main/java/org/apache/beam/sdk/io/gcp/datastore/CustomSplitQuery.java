package org.apache.beam.sdk.io.gcp.datastore;

import com.google.datastore.v1.Query;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

/**
 * Split query inspired by {@link DatastoreV1.Read.SplitQueryFn}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class CustomSplitQuery
        extends DatastoreV1.Read.SplitQueryFn
{
    public CustomSplitQuery( String projectId )
    {
        super( DatastoreV1.Read.V1Options.from( projectId, null, null ), 0 );
    }

    @StartBundle
    @Override
    public void startBundle( DoFn<Query, KV<Integer, Query>>.StartBundleContext c ) throws Exception
    {
        super.startBundle( c );
    }

    @ProcessElement
    @Override
    public void processElement( DoFn<Query, KV<Integer, Query>>.ProcessContext c ) throws Exception
    {
        super.processElement( c );
    }
}
