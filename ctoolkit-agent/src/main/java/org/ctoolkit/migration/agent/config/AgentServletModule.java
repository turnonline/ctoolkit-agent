package org.ctoolkit.migration.agent.config;

import com.google.api.server.spi.ServletInitializationParameters;
import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.appengine.tools.mapreduce.MapReduceServlet;
import com.google.appengine.tools.pipeline.impl.servlets.PipelineServlet;
import com.googlecode.objectify.ObjectifyFilter;
import org.ctoolkit.migration.agent.AccessControlAllowOrignFilter;
import org.ctoolkit.migration.agent.rest.AgentEndpointConfig;
import org.ctoolkit.migration.agent.rest.ChangeEndpoint;
import org.ctoolkit.migration.agent.rest.ExportEndpoint;
import org.ctoolkit.migration.agent.rest.ImportEndpoint;
import org.ctoolkit.migration.agent.rest.MetadataEndpoint;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

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

        // endpoints filter
        ServletInitializationParameters params = ServletInitializationParameters.builder()
                .addServiceClass( AgentEndpointConfig.class )
                .addServiceClass( ImportEndpoint.class )
                .addServiceClass( ChangeEndpoint.class )
                .addServiceClass( ExportEndpoint.class )
                .addServiceClass( MetadataEndpoint.class )
                // this is important, otherwise we cannot use certificates from third-party applications
                .setClientIdWhitelistEnabled( false ).build();
        serveGuiceSystemServiceServlet( "/_ah/spi/*", params );

        // objectify filter
        bind( ObjectifyFilter.class ).in( Singleton.class );
        filter( "/*" ).through( ObjectifyFilter.class );

        // access control filter
        bind( AccessControlAllowOrignFilter.class ).in( Singleton.class );
        filter( "/*" ).through( AccessControlAllowOrignFilter.class );

        // map reduce servlets
        bind( MapReduceServlet.class ).in( Singleton.class );
        serve( "/mapreduce/*" ).with( MapReduceServlet.class );

        bind( PipelineServlet.class ).in( Singleton.class );
        serve( "/_ah/pipeline/*" ).with( PipelineServlet.class );

        // appstats configuration
        bind( AppstatsServlet.class ).in( Singleton.class );
        bind( AppstatsFilter.class ).in( Singleton.class );

        serve( "/appstats/*" ).with( AppstatsServlet.class );

        // Appstats configuration
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put( "logMessage", "Appstats available: /appstats/details?time={ID}" );

        // exclude appstats itself from logging
        filterRegex( "^((?!/appstats/).)*$" ).through( AppstatsFilter.class, initParams );
    }
}
