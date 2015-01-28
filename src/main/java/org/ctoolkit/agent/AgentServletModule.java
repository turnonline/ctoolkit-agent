package org.ctoolkit.agent;

import com.google.api.server.spi.ServletInitializationParameters;
import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import org.ctoolkit.agent.restapi.AgentEndpointConfig;
import org.ctoolkit.agent.restapi.DataSetEndpoint;
import org.ctoolkit.agent.restapi.DiffSetEndpoint;
import org.ctoolkit.agent.restapi.EntitySchemaEndpoint;

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
                .addServiceClass( DataSetEndpoint.class )
                .addServiceClass( DiffSetEndpoint.class )
                .addServiceClass( EntitySchemaEndpoint.class )
                        // this is important, otherwise we cannot use certificates from third-party applications
                .setClientIdWhitelistEnabled( false ).build();

        this.serveGuiceSystemServiceServlet( "/_ah/spi/*", params );
    }
}
