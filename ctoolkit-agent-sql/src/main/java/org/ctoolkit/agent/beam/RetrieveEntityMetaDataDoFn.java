package org.ctoolkit.agent.beam;

import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.ApplicationContextFactory;
import org.ctoolkit.agent.service.WorkerService;

import java.util.List;

/**
 * Do function for retrieving list of {@link EntityExportData} from split sql query
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RetrieveEntityMetaDataDoFn
        extends DoFn<KV<MigrationSet, String>, KV<MigrationSet, List<EntityExportData>>>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        WorkerService service = ctx.getBean( WorkerService.class );

        List<EntityExportData> entityExportDataList = service.retrieveEntityMetaDataList( c.element().getValue() );
        c.output( KV.of( c.element().getKey(), entityExportDataList ) );
    }
}
