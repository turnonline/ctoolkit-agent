package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;

/**
 * Application context factory for sql workers
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ApplicationContextFactory
{
    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( ImportPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driverClassName", pipelineOptions.getJdbcDriver()
        ) );

        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }

    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( MigrationPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driverClassName", pipelineOptions.getJdbcDriver()
        ) );

        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }
}
