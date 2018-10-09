package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.ctoolkit.agent.converter.BaseConverterRegistrat;
import org.ctoolkit.agent.converter.ConverterRegistrat;
import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.converter.MongoConverterRegistrat;
import org.ctoolkit.agent.model.Agent;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter registrat factory provides map of {@link Agent} to {@link ConverterRegistrat} mapping
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class RegistratFactory
{
    @Bean
    @Singleton
    public Map<Agent, BaseConverterRegistrat> provideRegistrats(
            ElasticsearchConverterRegistrat elasticsearchConverterRegistrat,
            MongoConverterRegistrat mongoConverterRegistrat
    )
    {
        Map<Agent, BaseConverterRegistrat> registrats = new HashMap<>(  );
        registrats.put( Agent.ELASTICSEARCH, elasticsearchConverterRegistrat );
        registrats.put( Agent.MONGO, mongoConverterRegistrat );
        return registrats;
    }
}
