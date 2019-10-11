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

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.GqlQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import org.ctoolkit.agent.converter.KeyConverter;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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
    private Datastore datastore;

    @Inject
    private KeyConverter keyConverter;

    @Inject
    private ValueConverter valueConverter;

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        List<String> splitQueries = new ArrayList<>();

        String query = migrationSet.getQuery();

        // TODO: implement


        return splitQueries;
    }

    @SuppressWarnings( "unchecked" )
    public List<Export> executeQuery( String sql, Map<String, Object> namedParameters )
    {
        List<Export> exportList = new ArrayList<>();

        GqlQuery.Builder queryBuilder = Query.newGqlQueryBuilder( sql );
        Query<Entity> query = queryBuilder.build();

        QueryResults<Entity> results = datastore.run( query );
        results.forEachRemaining( entity -> {
            Export export = new Export();

            export.put( "key", keyConverter.convert( entity.getKey() ) );
            entity.getProperties().forEach( ( name, value )
                    -> export.putAll( valueConverter.convert( name, value ) ) );

            exportList.add( export );
        } );

        return exportList;
    }
}
