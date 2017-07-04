package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.PipelineResult;
import com.google.cloud.dataflow.sdk.runners.DataflowPipelineJob;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.service.ChangeSetService;

/**
 * Migration dataflow definition
 * 
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrationDataflowDefinition
        extends BaseDataflowDefinition<MigrationMetadata>
{
    public MigrationDataflowDefinition()
    {
        super();
    }

    public MigrationDataflowDefinition( Long metadataId )
    {
        super(metadataId, MigrationMetadata.class);
    }

    @Override
    public void run()
    {
        super.run();

        String kind = MigrationMetadata.class.getAnnotation( EntityMarker.class ).name();
        Key key = Key.newBuilder( projectId, kind, metadataId ).build();

        // reset metadata counters and state
        resetMetadata( key );

        // create pipeline
        Pipeline pipeline = Pipeline.create( pipelineOptions );

        // define pipelines
        pipeline
                .apply( new LoadItems<>( key, clazz, datastore ) )
                .apply( "Group item", ParDo.of( new DoFn<KeyValue, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        ChangeSetService changeSetService = injector().getInstance( ChangeSetService.class );
                        MigrationMetadataItem item = changeSetService.get( new MetadataItemKey<>( MigrationMetadataItem.class, c.element().get() ) );

                        // TODO: 1. group operations by kind
                        // TODO: 2. call changeSetService.migrate
                    }
                } ) );

        PipelineResult result = pipeline.run();

        if ( result instanceof DataflowPipelineJob )
        {
            DataflowPipelineJob jobResult = ( DataflowPipelineJob ) result;

            MigrationMetadata metadata = ModelConverter.convert( MigrationMetadata.class, datastore.get( key ) );
            metadata.setJobId( jobResult.getJobId() );
            metadata.save();
        }
    }
}
