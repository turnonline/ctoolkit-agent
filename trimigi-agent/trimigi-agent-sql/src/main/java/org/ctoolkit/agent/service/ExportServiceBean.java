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

import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.service.sql.Query;
import org.ctoolkit.agent.service.sql.SqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSetMetaData;
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
    private Provider<NamedParameterJdbcTemplate> jdbcTemplate;

    private SqlBuilder sqlBuilder = new SqlBuilder();

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        List<String> splitQueries = new ArrayList<>();
        MigrationSetSource source = migrationSet.getSource();

        Query query = new Query();
        query.setNamespace( source.getNamespace() );
        query.setKind( source.getKind() );
        query.setLimit( source.getLimit().intValue() );
        query.setOffset( source.getOffset().intValue() );
        query.setFilter( new ArrayList<>( source.getFilter() ) );

        // get split numbers
        String rootCountQuery = sqlBuilder.toSqlCount( query );

        jdbcTemplate.get().query( rootCountQuery, resultSet -> {
            long count = resultSet.getLong( 1 );

            // wee ned to split query into multiple offset + limit splitQueries
            if ( count > rowsPerSplit )
            {
                BigDecimal splits = BigDecimal.valueOf( count ).divide( BigDecimal.valueOf( rowsPerSplit ), RoundingMode.UP );

                for ( int offset = 0; offset < splits.doubleValue(); offset++ )
                {
                    // create offset + limit per split
                    Query splitQuery = new Query( query );
                    splitQuery.setOffset( offset );
                    splitQuery.setLimit( rowsPerSplit );

                    splitQueries.add( sqlBuilder.toSql( splitQuery ) );
                }
            }
            // noo need to split query, because there is less rows then rowsPerSplit
            else
            {
                splitQueries.add( sqlBuilder.toSql( query ) );
            }
        } );

        return splitQueries;
    }

    public List<Export> executeQuery( String query, Map<String, Object> namedParameters )
    {
        log.info( "Executing query: " + query + ", " + namedParameters );

        List<Export> exportList = new ArrayList<>();

        MapSqlParameterSource sqlNamedParameters = new MapSqlParameterSource();
        namedParameters.forEach( sqlNamedParameters::addValue );

        jdbcTemplate.get().query( query, sqlNamedParameters, resultSet -> {
            ResultSetMetaData metaData = resultSet.getMetaData();

            Export export = new Export();
            exportList.add( export );

            for ( int i = 1; i <= metaData.getColumnCount(); i++ )
            {
                export.put( metaData.getColumnName( i ), resultSet.getObject( i ) );
            }
        } );

        return exportList;
    }
}
