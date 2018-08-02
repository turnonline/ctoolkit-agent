package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.WorkerServiceBean;

import java.util.List;

/**
 * Do function for splitting queries from {@link MigrationSet}. It will produce KV of MigrationsSet>sql query
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class SplitQueriesDoFn
        extends DoFn<MigrationSet, KV<MigrationSet, String>>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        WorkerServiceBean service = new WorkerServiceBean( pipelineOptions );
        List<String> queries = service.splitQueries( c.element(), pipelineOptions.getRowsPerSplit() );

        for (String query : queries)
        {
            c.output( KV.of( c.element(), query) );
        }
    }
}
