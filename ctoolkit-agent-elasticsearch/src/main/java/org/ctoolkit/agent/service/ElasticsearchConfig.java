package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.http.HttpHost;
import org.ctoolkit.agent.beam.ElasticsearchPipelineOptions;
import org.ctoolkit.agent.converter.ConverterExecutor;
import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Elasticsearch configuration
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class ElasticsearchConfig
{
    private RestHighLevelClient client;

    private static final Logger log = LoggerFactory.getLogger( ElasticsearchConfig.class );

    @Bean
    @Singleton
    public RestHighLevelClient createClient( ElasticsearchPipelineOptions pipelineOptions )
    {
        String[] hosts = pipelineOptions.getElasticsearchHosts();
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for ( int i = 0; i < hosts.length; i++ )
        {
            httpHosts[i] = HttpHost.create( hosts[i] );
        }

        return client = new RestHighLevelClient( RestClient.builder( httpHosts ) );
    }

    @Bean
    @Singleton
    public ConverterExecutor createConverterExecutor()
    {
        return new ConverterExecutor( new ElasticsearchConverterRegistrat() );
    }

    @PreDestroy
    public void destroyClient()
    {
        try
        {
            client.close();
        }
        catch ( IOException e )
        {
            log.error( "Unable to close elasticearch client", e );
        }
    }
}
