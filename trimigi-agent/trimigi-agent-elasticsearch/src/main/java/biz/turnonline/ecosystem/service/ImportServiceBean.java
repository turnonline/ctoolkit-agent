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

package biz.turnonline.ecosystem.service;

import biz.turnonline.ecosystem.model.api.ImportSet;
import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.service.mapper.Mapper;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link ImportService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ImportServiceBean
        implements ImportService
{
    private static final Logger log = LoggerFactory.getLogger( ImportServiceBean.class );

    @Inject
    private RestHighLevelClient elasticClient;

    @Inject
    private Mapper<ImportSetProperty, Object> mapper;

    @Override
    public void importData( ImportSet importSet )
    {
        // drop index if requested
        if ( "DROP".equals( importSet.getClean() ) )
        {
            deleteIndex( importSet );
        }

        // import if namespace, kind and id is specified
        if ( importSet.getNamespace() != null && importSet.getKind() != null )
        {
            createIndex( importSet );
        }
    }

    // -- private helpers

    private void createIndex( ImportSet importSet )
    {
        try
        {
            Map<String, Object> jsonMap = new HashMap<>();
            for ( ImportSetProperty property : importSet.getProperties() )
            {
                mapper.map( property, jsonMap );
            }

            IndexRequest indexRequest = new IndexRequest( importSet.getNamespace(), importSet.getKind(), importSet.getId() );
            indexRequest.source( jsonMap );
            IndexResponse indexResponse = elasticClient.index( indexRequest );

            if ( indexResponse.status() != RestStatus.CREATED && indexResponse.status() != RestStatus.OK )
            {
                log.error( "Unexpected status. Expected: {},{} but was: {}", RestStatus.OK, RestStatus.CREATED, indexResponse.status() );
            }
        }
        catch ( IOException e )
        {
            log.error( "Unable to create index: " + importSet.getNamespace() + ":" + importSet.getKind(), e );
        }
    }

    private void deleteIndex( ImportSet importSet )
    {
        try
        {
            elasticClient.indices().delete( new DeleteIndexRequest( importSet.getNamespace() ) );
        }
        catch ( IOException e )
        {
            log.error( "Unable to delete index: " + importSet.getNamespace(), e );
        }
        catch ( ElasticsearchStatusException e )
        {
            if ( e.status() != RestStatus.NOT_FOUND )
            {
                log.error( "Unable to delete index.", e );
            }
        }
    }
}
