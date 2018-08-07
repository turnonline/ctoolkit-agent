package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.context.scope.ThreadLocal;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;

/**
 * Migration pipeline options provider
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class MigrationPipelineOptionsFactory
{
    @Bean
    @ThreadLocal
    public MigrationPipelineOptions getMigrationPipelineOptions()
    {
        return ApplicationContextFactory.options;
    }
}
