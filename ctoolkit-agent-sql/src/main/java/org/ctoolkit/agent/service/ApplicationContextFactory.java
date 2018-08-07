package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ApplicationContextFactory
{
    public static MigrationPipelineOptions options;

    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( MigrationPipelineOptions pipelineOptions )
    {
        options = pipelineOptions;

        return ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driver", pipelineOptions.getJdbcDriver()
        ) );
    }
}
