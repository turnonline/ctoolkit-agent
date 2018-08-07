package org.ctoolkit.agent.beam;

import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.ApplicationContextFactory;
import org.ctoolkit.agent.service.WorkerService;

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
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        WorkerService service = ctx.getBean( WorkerService.class );
        List<String> queries = service.splitQueries( c.element(), pipelineOptions.getRowsPerSplit() );

        for (String query : queries)
        {
            c.output( KV.of( c.element(), query) );
        }
    }
}
