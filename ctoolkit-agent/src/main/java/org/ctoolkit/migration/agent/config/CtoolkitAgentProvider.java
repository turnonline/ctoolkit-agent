package org.ctoolkit.migration.agent.config;

import org.ctoolkit.api.migration.CtoolkitAgent;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface CtoolkitAgentProvider
{
    CtoolkitAgent get();
}
