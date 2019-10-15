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

package biz.turnonline.ecosystem.datastore;

import biz.turnonline.ecosystem.converter.KeyConverter;
import com.google.datastore.v1.Filter;
import com.google.datastore.v1.PropertyFilter;
import com.google.datastore.v1.Query;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class GqlBuilder
{
    private final KeyConverter keyConverter;

    @Inject
    public GqlBuilder( KeyConverter keyConverter )
    {
        this.keyConverter = keyConverter;
    }

    private Map<String, PbValueToStringResolver> resolverMap = new HashMap<>();

    public String toGql( Query query )
    {
        StringBuilder gql = new StringBuilder();

        gql.append( "select * from " );
        gql.append( query.getKind( 0 ).getName() );
        gql.append( " " );

        if ( query.getOffset() > 0 )
        {
            gql.append( "offset " ).append( query.getOffset() );
        }

        if ( query.getLimit().getValue() > 0 )
        {
            gql.append( "limit " ).append( query.getLimit().getValue() );
        }

        if ( query.getFilter() != null )
        {
            List<Filter> filters = query.getFilter().getCompositeFilter().getFiltersList();
            if (!filters.isEmpty()) {
                gql.append( "where" );

                filters.forEach( filter -> {
                    PropertyFilter propertyFilter = filter.getPropertyFilter();
                    String valueType = propertyFilter.getValue().getValueTypeCase().name();

                    if ( resolverMap.containsKey( valueType ) )
                    {
                        String param = resolverMap.get( valueType ).resolve( propertyFilter.getValue() );

                        gql.append( " " );
                        gql.append( propertyFilter.getProperty().getName() );
                        gql.append( " " ).append( propertyFilter.getOp().name().replaceAll( "_", " " ) );
                        gql.append( " " ).append( param );
                    }
                } );
            }
        }

        return gql.toString();
    }

    @PostConstruct
    public void init()
    {
        resolverMap.put( "KEY_VALUE", value -> "KEY (" + keyConverter.convertFromPathElements( value.getKeyValue().getPathList() ) + ")" );
    }
}
