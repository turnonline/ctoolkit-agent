package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.PTransform;
import com.google.cloud.dataflow.sdk.values.PBegin;
import com.google.cloud.dataflow.sdk.values.PCollection;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.config.DataflowModule;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.ModelConverter;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Base dataflow definition
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class BaseDataflowDefinition<M extends BaseMetadata<?>>
        implements Runnable, Serializable
{
    @Inject
    protected static Injector injector;

    @Inject
    protected transient Datastore datastore;

    @Inject
    @ProjectId
    protected transient String projectId;

    @Inject
    protected transient PipelineOptions pipelineOptions;

    protected Long metadataId;

    protected Class<M> clazz;

    public BaseDataflowDefinition()
    {
    }

    public BaseDataflowDefinition( Long metadataId, Class<M> clazz )
    {
        this.metadataId = metadataId;
        this.clazz = clazz;
    }

    @Override
    public void run()
    {
        injector.injectMembers( this );
    }

    protected Injector injector()
    {
        return Guice.createInjector( new DataflowModule() );
    }

    // -- private helpers

    protected void resetMetadata( Key key )
    {
        M metadata = ModelConverter.convert( clazz, datastore.get( key ) );
        metadata.reset();
        metadata.save();
    }

    // -- common transforms

    /**
     * PTransform for loading BaseMetadata items
     */
    public class LoadItems<M extends BaseMetadata<?>>
            extends PTransform<PBegin, PCollection<KeyValue>>
    {
        private Datastore datastore;

        private Key key;

        private Class<M> clazz;

        public LoadItems( Key key, Class<M> clazz, Datastore datastore )
        {
            super( "Load items" );

            this.key = key;
            this.clazz = clazz;
            this.datastore = datastore;
        }

        @Override
        public PCollection<KeyValue> apply( PBegin input )
        {
            Entity entity = datastore.get( key );
            M metadata = ModelConverter.convert( clazz, entity );

            return input.apply( Create.of( metadata.getItemsKeyValue() ) );
        }
    }
}
