package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationSet;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Migration beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationBeamPipeline
        extends BeamPipeline<MigrationBatch, MigrationPipelineOptions>
{
    @Inject
    private DoFnFactory doFnFactory;

    @Override
    public Pipeline create( final MigrationBatch batch, MigrationPipelineOptions options )
    {
        Pipeline pipeline = Pipeline.create( options );
        pipeline
                .apply( "Split migration sets", new PTransform<PBegin, PCollection<MigrationSet>>()
                {
                    @Override
                    public PCollection<MigrationSet> expand( PBegin input )
                    {
                        return input.apply( Create.of( batch.getMigrationSets() ) );
                    }
                } )
                .apply( "Split queries", ParDo.of( doFnFactory.createSplitQueriesDoFn() ) )
                .apply( "Retrieve entity metadata list", ParDo.of( doFnFactory.createRetrieveEntityMetadataListDoFn() ) )
                .apply( "Transform to import set and import to target agent", ParDo.of( doFnFactory.createTransformAndImportDoFn() ) );

        return pipeline;
    }
}
