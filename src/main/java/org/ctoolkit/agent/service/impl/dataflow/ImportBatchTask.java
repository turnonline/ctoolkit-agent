package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.PTransform;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.dataflow.sdk.values.PBegin;
import com.google.cloud.dataflow.sdk.values.PCollection;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.inject.Injector;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportBatchTask
        implements Serializable
{
    @Inject
    protected static Injector injector;

    @Inject
    private transient Datastore datastore;

    @Inject
    @ProjectId
    private transient String projectId;

    @Inject
    private transient PipelineOptions pipelineOptions;

    private Long metadataId;

    public ImportBatchTask()
    {
    }

    public ImportBatchTask( Long metadataId )
    {
        this.metadataId = metadataId;
    }

    public void run()
    {
        injector.injectMembers( this );

        String kind = ImportMetadata.class.getAnnotation( EntityMarker.class ).name();
        Key key = Key.newBuilder( projectId, kind, metadataId ).build();

        // create pipeline
        Pipeline pipeline = Pipeline.create( pipelineOptions );

        // define pipelines
        pipeline
                .apply( new LoadItems( key, datastore ) )
                .apply( "Process item", ParDo.of( new DoFn<KeyValue, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        ChangeSetService changeSetService = injector.getInstance( ChangeSetService.class );
                        ImportMetadataItem item = changeSetService.get( new MetadataItemKey<>( ImportMetadataItem.class, c.element().get() ) );

                        JobState jobState;
                        String error = null;

                        // import change set
                        try
                        {
                            changeSetService.importChangeSet( item.toChangeSet() );

                            error = null;
                            jobState = JobState.COMPLETED_SUCCESSFULLY;
                        }
                        catch ( Exception e )
                        {
                            e.printStackTrace(); // TODO: remove

                            // TODO: revert
                            // error = StackTraceResolver.resolve( e );
                            jobState = JobState.STOPPED_BY_ERROR;
                        }

                        // update status in item
                        item.setState( jobState );
                        item.setError( error );
                        item.saveFieldsOnly();

                        // TODO: increment parent
                    }
                } ) );

        pipeline.run();
    }

    public class LoadItems
            extends PTransform<PBegin, PCollection<KeyValue>>
    {
        private Datastore datastore;

        private Key key;

        public LoadItems( Key key, Datastore datastore )
        {
            super( "Load items" );

            this.key = key;
            this.datastore = datastore;
        }

        @Override
        public PCollection<KeyValue> apply( PBegin input )
        {
            Entity entity = datastore.get( key );
            ImportMetadata importMetadata = ModelConverter.convert( ImportMetadata.class, entity );

            return input.apply( Create.of( importMetadata.getItemsKeyValue() ) );
        }
    }
}
