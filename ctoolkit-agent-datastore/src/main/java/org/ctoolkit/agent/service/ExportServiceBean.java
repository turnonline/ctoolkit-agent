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

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.GqlQuery;
import com.google.cloud.datastore.QueryResults;
import com.google.datastore.v1.CompositeFilter;
import com.google.datastore.v1.Filter;
import com.google.datastore.v1.Key;
import com.google.datastore.v1.KindExpression;
import com.google.datastore.v1.PartitionId;
import com.google.datastore.v1.PropertyFilter;
import com.google.datastore.v1.PropertyReference;
import com.google.datastore.v1.Query;
import com.google.datastore.v1.Value;
import com.google.datastore.v1.client.Datastore;
import com.google.datastore.v1.client.DatastoreException;
import com.google.datastore.v1.client.QuerySplitter;
import com.google.protobuf.Int32Value;
import org.ctoolkit.agent.converter.KeyConverter;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.datastore.QueryParser;
import org.ctoolkit.agent.datastore.StringToQueryPbValueResolver;
import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.RawKey;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ExportService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ExportServiceBean
        implements ExportService
{
    private static final Logger log = LoggerFactory.getLogger( ExportServiceBean.class );

    @Inject
    private Datastore pbDatastore;

    @Inject
    private com.google.cloud.datastore.Datastore datastore;

    @Inject
    private QuerySplitter querySplitter;

    @Inject
    private KeyConverter keyConverter;

    @Inject
    private ValueConverter valueConverter;

    @Inject
    private QueryParser queryParser;

    private Map<String, PropertyFilter.Operator> operatorMap = new HashMap<>();
    private Map<String, StringToQueryPbValueResolver> valueTypeResolverMap = new HashMap<>();

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        List<String> splits = new ArrayList<>();

        // https://www.programcreek.com/java-api-examples/?class=com.google.datastore.v1.Query&method=Builder
        MigrationSetSource source = migrationSet.getSource();

        Query.Builder queryBuilder = Query.newBuilder()
                .addKind( KindExpression.newBuilder()
                        .setName( source.getKind() )
                        .build() );

        if ( source.getLimit() > 0 )
        {
            queryBuilder.setLimit( Int32Value.newBuilder().setValue( source.getLimit().intValue() ).build() );
        }
        if ( source.getOffset() > 0 )
        {
            queryBuilder.setOffset( source.getOffset().intValue() );
        }

        CompositeFilter.Builder compositeFilter = CompositeFilter.newBuilder();
        source.getFilter().forEach( queryFilter ->
                compositeFilter
                        .addFilters( Filter.newBuilder()
                                .setPropertyFilter( PropertyFilter.newBuilder()
                                        .setProperty( PropertyReference.newBuilder().setName( queryFilter.getName() ).build() )
                                        .setOp( operatorMap.get( queryFilter.getOperation() ) )
                                        .setValue( valueTypeResolverMap
                                                .get( queryFilter.getValueType() )
                                                .resolve( queryFilter.getValue() )
                                        )
                                        .build() )
                                .build() )
        );

        queryBuilder.setFilter( Filter.newBuilder().setCompositeFilter( compositeFilter ) );
        Query query = queryBuilder.build();

        try
        {
            List<Query> querySplits = querySplitter.getSplits( query, PartitionId.getDefaultInstance(), rowsPerSplit, pbDatastore );
            querySplits.forEach( query1 -> splits.add( queryParser.toGql( query1 ) ) );
        }
        catch ( DatastoreException e )
        {
            log.error( "Unable to run query - " + query, e );
        }

        return splits;
    }

    @SuppressWarnings( "unchecked" )
    public List<Export> executeQuery( String query, Map<String, Object> namedParameters )
    {
        log.info( "Executing query: " + query );

        List<Export> exportList = new ArrayList<>();
        GqlQuery.Builder queryBuilder = com.google.cloud.datastore.Query.newGqlQueryBuilder( query );
        queryBuilder.setAllowLiteral( true );

        QueryResults<Entity> results = datastore.run( queryBuilder.build() );
        results.forEachRemaining( entity -> {
            Export export = new Export();

            export.put( "__key__", new RawKey( keyConverter.convertFromRawKey( entity.getKey() ) ) );
            entity.getProperties().forEach( ( name, value )
                    -> export.putAll( valueConverter.fromValue( name, value ) ) );

            exportList.add( export );
        } );

        return exportList;
    }

    @PostConstruct
    public void init()
    {
        operatorMap.put( "=", PropertyFilter.Operator.EQUAL );
        operatorMap.put( "<", PropertyFilter.Operator.LESS_THAN );
        operatorMap.put( "<=", PropertyFilter.Operator.LESS_THAN_OR_EQUAL );
        operatorMap.put( ">", PropertyFilter.Operator.GREATER_THAN );
        operatorMap.put( "=>", PropertyFilter.Operator.GREATER_THAN_OR_EQUAL );
        operatorMap.put( "^", PropertyFilter.Operator.HAS_ANCESTOR );

        valueTypeResolverMap.put( "key", ( value ) ->
                Value.newBuilder().setKeyValue(
                        Key.newBuilder().addAllPath( keyConverter.convertToPathElements( value ) )
                ).build() );
    }
}