package org.ctoolkit.agent.service.impl.dataflow.shared;

import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
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
        if ( injector != null )
        {
            return injector;
        }

        return Guice.createInjector( new DataflowModule() );
    }

    protected Datastore datastore()
    {
        return injector().getInstance( Datastore.class );
    }

    // -- private helpers

    protected void resetMetadata( Key key )
    {
        M metadata = ModelConverter.convert( clazz, datastore.get( key ) );
        metadata.reset();
        metadata.save();
    }
}
