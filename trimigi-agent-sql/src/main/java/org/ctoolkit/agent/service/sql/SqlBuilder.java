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

package org.ctoolkit.agent.service.sql;

import org.ctoolkit.agent.model.api.QueryFilter;

import java.util.List;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class SqlBuilder
{
    public String toSql( Query query )
    {
        StringBuilder sql = toSqlInternal( query, "*" );

        limit( sql, query.getLimit() );
        offset( sql, query.getOffset() );

        return sql.toString();
    }

    public String toSqlCount( Query query )
    {
        return toSqlInternal( query, "count(0)" ).toString();
    }

    protected void limit( StringBuilder sql, long limit )
    {
        if ( limit > 0 )
        {
            sql.append( " limit " ).append( limit );
        }
    }

    protected void offset( StringBuilder sql, long offset )
    {
        if ( offset > 0 )
        {
            sql.append( " offset " ).append( offset );
        }
    }

    // -- private helpers

    private StringBuilder toSqlInternal( Query query, String column )
    {
        String schema = query.getNamespace();
        String table = query.getKind();

        List<QueryFilter> filters = query.getFilter();

        StringBuilder sql = new StringBuilder();
        sql.append( "select " ).append( column ).append( " from " );
        sql.append( schema ).append( "." ).append( table );

        if ( !filters.isEmpty() )
        {
            sql.append( " where " );

            StringBuilder conditions = new StringBuilder();

            filters.forEach( filter -> {
                        if ( conditions.length() > 0 )
                        {
                            conditions.append( " and " );
                        }
                        conditions
                                .append( filter.getName() ).append( " " )
                                .append( filter.getOperation() ).append( " " )
                                .append( filter.getValue() );
                    }
            );

            sql.append( conditions );
        }

        return sql;
    }
}
