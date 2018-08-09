package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportSet;

import javax.inject.Inject;
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
    @Inject
    private DoFnFactory doFnFactory;

    @Override
    public Pipeline create( ImportBatch batch, ImportPipelineOptions options )
    {
        Pipeline pipeline = Pipeline.create( options );
        pipeline
                .apply( "Split import sets", new PTransform<PBegin, PCollection<ImportSet>>()
                {
                    @Override
                    public PCollection<ImportSet> expand( PBegin input )
                    {
                        return input.apply( Create.of( batch.getImportSets() ) );
                    }
                } )
                .apply( "Import sets", ParDo.of( doFnFactory.createImportDoFn() ) );

        return pipeline;
    }
}
