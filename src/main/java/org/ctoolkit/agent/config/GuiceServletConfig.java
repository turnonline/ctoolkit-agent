package org.ctoolkit.agent.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.ctoolkit.agent.AgentModule;

/**
 * Application guice module.
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class GuiceServletConfig
        extends com.google.inject.servlet.GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( Stage.DEVELOPMENT, new AgentModule() );
    }
}
