package org.ctoolkit.migration.agent.config;

import com.google.api.server.spi.ServletInitializationParameters;
import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.google.appengine.tools.mapreduce.MapReduceServlet;
import com.google.appengine.tools.pipeline.impl.servlets.PipelineServlet;
import com.googlecode.objectify.ObjectifyFilter;
import org.ctoolkit.migration.agent.rest.AgentEndpointConfig;
import org.ctoolkit.migration.agent.rest.ChangeEndpoint;
import org.ctoolkit.migration.agent.rest.ExportEndpoint;
import org.ctoolkit.migration.agent.rest.ImportEndpoint;
import org.ctoolkit.migration.agent.rest.MetadataEndpoint;

import javax.inject.Singleton;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentServletModule
        extends GuiceSystemServiceServletModule
{
    @Override
    protected void configureServlets()
    {
        super.configureServlets();

        ServletInitializationParameters params = ServletInitializationParameters.builder()
                .addServiceClass( AgentEndpointConfig.class )
                .addServiceClass( ImportEndpoint.class )
                .addServiceClass( ChangeEndpoint.class )
                .addServiceClass( ExportEndpoint.class )
                .addServiceClass( MetadataEndpoint.class )
                // this is important, otherwise we cannot use certificates from third-party applications
                .setClientIdWhitelistEnabled( false ).build();

        this.serveGuiceSystemServiceServlet( "/_ah/spi/*", params );

        bind( ObjectifyFilter.class ).in( Singleton.class );
        filter( "/*" ).through( ObjectifyFilter.class );

        bind( MapReduceServlet.class ).in( Singleton.class );
        serve( "/mapreduce/*" ).with( MapReduceServlet.class );

        bind( PipelineServlet.class ).in( Singleton.class );
        serve( "/_ah/pipeline/*" ).with( PipelineServlet.class );
    }
}
