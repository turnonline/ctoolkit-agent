package org.ctoolkit.agent.service.impl;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class DataflowTest
        implements Serializable
{
    @Test
    public void test()
    {
        DataflowPipelineOptions options = PipelineOptionsFactory.create().as( DataflowPipelineOptions.class );
        Pipeline pipeline = Pipeline.create( options );

        pipeline
                .apply( "Load entities", Create.of( "foo", "bar", "baz", "cool" ) )
                .apply( "Process entity", ParDo.of( new DoFn<String, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c )
                    {
                        String e = c.element();
                        System.out.println( Thread.currentThread().getId() + " - String: " + e );
                    }
                } ) );

        pipeline.run();
    }
}
