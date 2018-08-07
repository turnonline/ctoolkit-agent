package org.ctoolkit.agent.beam;

import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.ApplicationContextFactory;
import org.ctoolkit.agent.service.MigrationService;

import java.util.List;

/**
 * Do function for transform list of {@link EntityMetaData} to {@link ImportBatch} using {@link MigrationSet}
 * and than import it to target agent
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class TransformAndImportDoFn
        extends DoFn<KV<MigrationSet, List<EntityMetaData>>, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        MigrationService service = ctx.getBean( MigrationService.class );

        // transform to ImportBatch
        ImportBatch importBatch = service.transform( c.element().getKey(), c.element().getValue() );

        // import to target agent
        service.importToTargetAgent( importBatch );
    }
}
