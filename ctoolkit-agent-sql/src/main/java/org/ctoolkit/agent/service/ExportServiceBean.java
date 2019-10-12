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

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.util.SelectUtils;
import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.sql.CountColumn;
import org.ctoolkit.agent.service.sql.VendorIndependentLimit;
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
import java.util.Collections;
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

    // TODO: refactor - implement offset, limit and filter - probably introduce QueryBuilder and remove 'query' if
    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        List<String> splitQueries = new ArrayList<>();

        String query = null;
        Select rootSelect;
        Select rootCountSelect;

        // create select as 'select * from sourceNamespace.sourceKind'
        if ( query == null )
        {
            Table table = new Table( migrationSet.getSource().getNamespace(), migrationSet.getSource().getKind() );
            rootSelect = SelectUtils.buildSelectFromTable( table );
            rootCountSelect = SelectUtils.buildSelectFromTable( table );
        }
        // create select as provided by query in MigrationSet
        else
        {
            try
            {
                rootSelect = ( Select ) CCJSqlParserUtil.parse( query );
                rootCountSelect = ( Select ) CCJSqlParserUtil.parse( query );
            }
            catch ( JSQLParserException e )
            {
                log.error( "Unable to parse root query: " + query, e );
                throw new RuntimeException( "Unable to parse root query: " + query, e );
            }
        }

        // replace select items with 'select count(*) ...'
        rootCountSelect.getSelectBody().accept( new SelectVisitorAdapter()
        {
            @Override
            public void visit( PlainSelect plainSelect )
            {
                plainSelect.setSelectItems( Collections.singletonList( new CountColumn() ) );
            }
        } );

        // get split numbers
        String rootCountQuery = rootCountSelect.toString();

        jdbcTemplate.get().query( rootCountQuery, resultSet -> {
            int count = resultSet.getInt( 1 );

            // wee ned to split query into multiple offset + limit splitQueries
            if ( count > rowsPerSplit )
            {
                BigDecimal splits = BigDecimal.valueOf( count ).divide( BigDecimal.valueOf( rowsPerSplit ), RoundingMode.UP );

                for ( int offset = 0; offset < splits.doubleValue(); offset++ )
                {
                    // create offset + limit per split
                    rootSelect.getSelectBody().accept( new VendorIndependentLimit( offset, rowsPerSplit ) );
                    splitQueries.add( rootSelect.toString() );
                }
            }
            // noo need to split query, because there is less rows then rowsPerSplit
            else
            {
                splitQueries.add( rootSelect.toString() );
            }
        } );

        return splitQueries;
    }

    public List<Export> executeQuery( String query, Map<String, Object> namedParameters )
    {
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
