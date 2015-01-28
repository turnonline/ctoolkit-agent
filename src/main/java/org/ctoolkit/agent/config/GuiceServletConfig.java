package org.ctoolkit.agent.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.ctoolkit.agent.AgentModule;
import org.ctoolkit.agent.AgentServletModule;

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
        return Guice.createInjector( Stage.DEVELOPMENT,
                new AgentModule(),
                new AgentServletModule() );
    }
}
