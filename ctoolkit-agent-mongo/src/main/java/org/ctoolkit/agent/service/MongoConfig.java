package org.ctoolkit.agent.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.ctoolkit.agent.beam.MongoPipelineOptions;
import org.ctoolkit.agent.converter.ConverterExecutor;
import org.ctoolkit.agent.converter.MongoConverterRegistrat;

import javax.inject.Singleton;

/**
 * Mongo configuration
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class MongoConfig
{
    @Bean
    @Singleton
    public MongoClient createClient( MongoPipelineOptions pipelineOptions )
    {
        MongoClientURI connectionString = new MongoClientURI( pipelineOptions.getMongoUri() );
        return new MongoClient( connectionString );
    }

    @Bean
    @Singleton
    public ConverterExecutor createConverterExecutor()
    {
        return new ConverterExecutor( new MongoConverterRegistrat() );
    }
}
