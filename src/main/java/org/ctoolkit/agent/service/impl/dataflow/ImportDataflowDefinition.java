package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.PipelineResult;
import com.google.cloud.dataflow.sdk.runners.DataflowPipelineJob;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.CounterCallback;
import org.ctoolkit.agent.service.impl.dataflow.shared.BaseDataflowDefinition;
import org.ctoolkit.agent.service.impl.dataflow.shared.LoadItems;

/**
 * Import dataflow definition
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportDataflowDefinition
        extends BaseDataflowDefinition<ImportMetadata>
{
    public ImportDataflowDefinition()
    {
        super();
    }

    public ImportDataflowDefinition( Long metadataId )
    {
        super( metadataId, ImportMetadata.class );
    }

    @Override
    public void run()
    {
        super.run();

        String kind = ImportMetadata.class.getAnnotation( EntityMarker.class ).name();
        Key key = Key.newBuilder( projectId, kind, metadataId ).build();

        // reset metadata counters and state
        resetMetadata( key );

        // create pipeline
        Pipeline pipeline = Pipeline.create( pipelineOptions );

        // define pipelines
        pipeline
                .apply( new LoadItems<>( key, clazz, datastore ) )
                .apply( "Process item", ParDo.of( new DoFn<KeyValue, String>()
                {
                    @Override
                    public void processElement( final ProcessContext c ) throws Exception
                    {
                        ChangeSetService changeSetService = injector().getInstance( ChangeSetService.class );
                        ImportMetadataItem item = changeSetService.get( new MetadataItemKey<>( ImportMetadataItem.class, c.element().get() ) );

                        try
                        {
                            // import change set
                            changeSetService.importChangeSet( item.unmarshallData( ChangeSet.class ), new CounterCallback()
                            {
                                @Override
                                public void onCallback()
                                {
                                    c.output( "x" ); // send dummy data, otherwise we will not be able to find out processed items
                                }
                            } );

                            // set state to COMPLETED_SUCCESSFULLY
                            item.setState( JobState.DONE );
                        }
                        catch ( Exception e )
                        {
                            log().error( "Error occur during importing change set", e );

                            //item.setError( StackTraceResolver.resolve( e ) );
                            item.setState( JobState.FAILED );
                        }

                        // update status in item
                        item.saveFieldsOnly();

                    }
                } ) )
                .apply( "Log item", ParDo.of( new DoFn<String, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        log().info( "Processed item: " + c.element() );
                    }
                } ) );

        PipelineResult result = pipeline.run();

        if ( result instanceof DataflowPipelineJob )
        {
            DataflowPipelineJob jobResult = ( DataflowPipelineJob ) result;

            ImportMetadata metadata = ModelConverter.convert( ImportMetadata.class, datastore.get( key ) );
            metadata.setJobId( jobResult.getJobId() );
            metadata.save();
        }
    }
}
