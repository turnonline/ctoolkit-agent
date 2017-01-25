package org.ctoolkit.migration.agent.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

/**
 * Application guice module.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class GuiceServletConfig
        extends com.google.inject.servlet.GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( Stage.PRODUCTION,
                new AgentModule(),
                new AgentServletModule(),
                new IAMModule() );
    }
}
