package org.ctoolkit.agent.beam;

import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.ApplicationContextFactory;
import org.ctoolkit.agent.service.MigrationService;

import java.util.List;

/**
 * Do function for transform list of {@link EntityExportData} to {@link ImportBatch} using {@link MigrationSet}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class TransformAndImportDoFn
        extends DoFn<KV<MigrationSet, List<EntityExportData>>, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        MigrationService service = ctx.getBean( MigrationService.class );

        // transform to import sets
        List<ImportSet> importSets = service.transform( c.element().getKey(), c.element().getValue() );

        // import import sets into target agent
        service.importToTargetAgent( importSets );
    }
}
