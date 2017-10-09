package org.ctoolkit.agent.config;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import org.ctoolkit.agent.annotation.ProjectId;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class DataflowOptionsConfigurator
{
    @Inject
    private Datastore datastore;

    @Inject
    @ProjectId
    private String projectId;

    public void configure( String name, Configurator configurator )
    {
        Entity entity = datastore.get( new KeyFactory( projectId ).setKind( "Property" ).newKey( name ) );

        if ( entity != null && entity.contains( "value" ) )
        {
            configurator.configure( entity.getString( "value" ) );
        }
    }

    public interface Configurator
    {
        void configure( String property );
    }
}
