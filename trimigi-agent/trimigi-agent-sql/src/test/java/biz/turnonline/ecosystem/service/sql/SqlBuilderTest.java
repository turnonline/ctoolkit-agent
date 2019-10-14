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

package biz.turnonline.ecosystem.service.sql;

import biz.turnonline.ecosystem.model.api.QueryFilter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class SqlBuilderTest
{
    private SqlBuilder sqlBuilder = new SqlBuilder();

    // -- sql

    @Test
    public void toSql_Simple()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        assertEquals( "select * from global.person", sqlBuilder.toSql( query ) );
    }

    @Test
    public void toSql_LimitOffset()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );
        query.setLimit( 10 );
        query.setOffset( 2 );

        assertEquals( "select * from global.person limit 10 offset 2", sqlBuilder.toSql( query ) );
    }

    @Test
    public void toSql_FilterOneItem()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        QueryFilter filter = new QueryFilter();
        filter.setName( "name" );
        filter.setOperation( "=" );
        filter.setValue( "'John'" );
        query.getFilter().add( filter );

        assertEquals( "select * from global.person where name = 'John'", sqlBuilder.toSql( query ) );
    }

    @Test
    public void toSql_FilterMultipleItem()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        QueryFilter nameFilter = new QueryFilter();
        nameFilter.setName( "name" );
        nameFilter.setOperation( "=" );
        nameFilter.setValue( "'John'" );
        query.getFilter().add( nameFilter );

        QueryFilter ageFilter = new QueryFilter();
        ageFilter.setName( "age" );
        ageFilter.setOperation( ">" );
        ageFilter.setValue( "35" );
        query.getFilter().add( ageFilter );

        assertEquals( "select * from global.person where name = 'John' and age > 35", sqlBuilder.toSql( query ) );
    }

    // -- count

    @Test
    public void toSqlCount_Simple()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        assertEquals( "select count(0) from global.person", sqlBuilder.toSqlCount( query ) );
    }

    @Test
    public void toSqlCount_LimitOffset()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );
        query.setLimit( 10 );
        query.setOffset( 2 );

        assertEquals( "select count(0) from global.person", sqlBuilder.toSqlCount( query ) );
    }

    @Test
    public void toSqlCount_FilterOneItem()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        QueryFilter filter = new QueryFilter();
        filter.setName( "name" );
        filter.setOperation( "=" );
        filter.setValue( "'John'" );
        query.getFilter().add( filter );

        assertEquals( "select count(0) from global.person where name = 'John'", sqlBuilder.toSqlCount( query ) );
    }

    @Test
    public void toSqlCount_FilterMultipleItem()
    {
        Query query = new Query();
        query.setNamespace( "global" );
        query.setKind( "person" );

        QueryFilter nameFilter = new QueryFilter();
        nameFilter.setName( "name" );
        nameFilter.setOperation( "=" );
        nameFilter.setValue( "'John'" );
        query.getFilter().add( nameFilter );

        QueryFilter ageFilter = new QueryFilter();
        ageFilter.setName( "age" );
        ageFilter.setOperation( ">" );
        ageFilter.setValue( "35" );
        query.getFilter().add( ageFilter );

        assertEquals( "select count(0) from global.person where name = 'John' and age > 35", sqlBuilder.toSqlCount( query ) );
    }
}