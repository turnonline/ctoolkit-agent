package org.ctoolkit.agent.rest;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.logging.Logger;

/**
 * Agent rest application configuration - it registers packages which contains rest endpoints and acts
 * as a glue between guice and jersey DI
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AgentApplication
        extends ResourceConfig
{
    public static final String PACKAGES = "org.ctoolkit.agent.rest";

    @Inject
    public AgentApplication( ServiceLocator serviceLocator, ServletContext servletContext )
    {
        packages( PACKAGES );
        register( new LoggingFilter( Logger.getLogger( LoggingFilter.class.getName() ), true ) );

        GuiceBridge.getGuiceBridge().initializeGuiceBridge( serviceLocator );
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService( GuiceIntoHK2Bridge.class );
        Injector injector = ( Injector ) servletContext.getAttribute( Injector.class.getName() );
        if ( injector == null )
        {
            throw new RuntimeException( "Guice Injector not found" );
        }

        guiceBridge.bridgeGuiceInjector( injector );
    }
}
