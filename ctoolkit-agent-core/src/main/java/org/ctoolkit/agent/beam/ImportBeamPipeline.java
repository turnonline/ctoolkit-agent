package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.ctoolkit.agent.model.api.ImportBatch;

import javax.inject.Singleton;

/**
 * Import beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ImportBeamPipeline
        extends BeamPipeline<ImportBatch, ImportPipelineOptions>
{
    @Override
    public Pipeline create( ImportBatch batch, ImportPipelineOptions options )
    {
        Pipeline pipeline = Pipeline.create( options );
        // TODO: implement

        return pipeline;
    }
}
