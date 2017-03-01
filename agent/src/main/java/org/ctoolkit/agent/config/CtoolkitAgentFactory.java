package org.ctoolkit.agent.config;

import org.ctoolkit.agent.model.CtoolkitAgentConfiguration;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface CtoolkitAgentFactory
{
    CtoolkitAgentProvider provideCtoolkitAgent( CtoolkitAgentConfiguration configuration );
}
