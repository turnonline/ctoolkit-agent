package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.ctoolkit.agent.converter.BaseConverterRegistrat;
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
    public Map<Agent, BaseConverterRegistrat> provideRegistrats( ElasticsearchConverterRegistrat elasticsearchConverterRegistrat)
    {
        Map<Agent, BaseConverterRegistrat> registrats = new HashMap<>(  );
        registrats.put( Agent.ELASTICSEARCH, elasticsearchConverterRegistrat );
        return registrats;
    }
}
