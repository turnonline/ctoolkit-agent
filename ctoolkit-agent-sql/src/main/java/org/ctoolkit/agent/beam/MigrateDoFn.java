package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.WorkerServiceBean;

import java.util.List;

/**
 * Do function for migrate data to other agent
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MigrateDoFn
        extends DoFn<KV<MigrationSet, List<EntityMetaData>>, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        WorkerServiceBean service = new WorkerServiceBean( pipelineOptions );
        service.migrate( c.element().getKey(), c.element().getValue() );
    }
}
