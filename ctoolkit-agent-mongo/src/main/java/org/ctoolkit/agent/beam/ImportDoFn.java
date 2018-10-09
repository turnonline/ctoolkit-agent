package org.ctoolkit.agent.beam;

import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.service.ApplicationContextFactory;
import org.ctoolkit.agent.service.WorkerService;

/**
 * Do function for importing of {@link ImportSet}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportDoFn
        extends DoFn<ImportSet, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        ImportPipelineOptions pipelineOptions = c.getPipelineOptions().as( ImportPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        WorkerService service = ctx.getBean( WorkerService.class );

        service.importData( c.element() );
    }
}
