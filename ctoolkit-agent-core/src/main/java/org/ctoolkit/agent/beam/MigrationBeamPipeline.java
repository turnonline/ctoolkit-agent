package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.ctoolkit.agent.model.api.MigrationBatch;

import javax.inject.Singleton;
import java.util.Arrays;

/**
 * Migration beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationBeamPipeline
        extends BeamPipeline<MigrationBatch, MigrationPipelineOptions>
{
    @Override
    public Pipeline create( MigrationBatch batch, MigrationPipelineOptions options )
    {
        Pipeline pipeline = Pipeline.create( options );
        // TODO: implement
        pipeline
                .apply( new PTransform<PBegin, PCollection<String>>()
                {
                    @Override
                    public PCollection<String> expand( PBegin input )
                    {
                        return input.apply( Create.of( Arrays.asList( "john", "foo", "man", "dick" ) ) );
                    }
                } )
                .apply( ParDo.of( new DoFn<String, Void>()
                {
                    @ProcessElement
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        log().error( "Name: " + c.element() );
                    }
                } ) );

        return pipeline;
    }
}
