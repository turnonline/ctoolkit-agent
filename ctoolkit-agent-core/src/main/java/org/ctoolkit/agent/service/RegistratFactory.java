package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.ctoolkit.agent.converter.ConverterRegistrat;
import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.model.Agent;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class RegistratFactory
{
    @Bean
    @Singleton
    public Map<Agent, ConverterRegistrat> provideRegistrats(ElasticsearchConverterRegistrat elasticsearchConverterRegistrat)
    {
        Map<Agent, ConverterRegistrat> registrats = new HashMap<>(  );
        registrats.put( Agent.ELASTICSEARCH, elasticsearchConverterRegistrat );
        return registrats;
    }
}
