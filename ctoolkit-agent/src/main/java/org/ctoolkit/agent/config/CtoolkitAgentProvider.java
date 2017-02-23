package org.ctoolkit.agent.config;

import org.ctoolkit.api.agent.CtoolkitAgent;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface CtoolkitAgentProvider
{
    CtoolkitAgent get();
}
