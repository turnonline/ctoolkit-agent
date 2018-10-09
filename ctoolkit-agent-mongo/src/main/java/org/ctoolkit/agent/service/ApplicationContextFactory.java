package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;

/**
 * Application context factory for elasticsearch workers
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ApplicationContextFactory
{
    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( ImportPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run();
        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }

    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( MigrationPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run();
        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }
}
