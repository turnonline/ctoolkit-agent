/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.http.HttpHost;
import org.ctoolkit.agent.service.beam.options.ElasticsearchPipelineOptions;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.service.mapper.ImportSetPropertyToMapMapper;
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
public class ElasticsearchAgentConfig
{
    private RestHighLevelClient client;

    private static final Logger log = LoggerFactory.getLogger( ElasticsearchAgentConfig.class );

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

    @Bean
    @Singleton
    public ImportSetPropertyToMapMapper createImportSetPropertyMapper( ConverterExecutor converterExecutor )
    {
        return new ImportSetPropertyToMapMapper( converterExecutor );
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
            log.error( "Unable to close elasticsearch client", e );
        }
    }
}
