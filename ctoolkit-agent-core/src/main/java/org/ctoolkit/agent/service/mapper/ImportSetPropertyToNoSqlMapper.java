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

package org.ctoolkit.agent.service.mapper;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.service.converter.ConverterExecutor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ImportSetProperty} to NoSql structures mapper. It is expected that NoSql database
 * structure is created via Map structure
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportSetPropertyToNoSqlMapper
        implements Mapper<ImportSetProperty, Object>
{
    private final ConverterExecutor converterExecutor;

    public ImportSetPropertyToNoSqlMapper( ConverterExecutor converterExecutor )
    {
        this.converterExecutor = converterExecutor;
    }

    @Override
    public void map( ImportSetProperty importSetProperty, Object parentElement )
    {
        addProperty( importSetProperty, parentElement );
    }

    @SuppressWarnings( "unchecked" )
    private void addProperty( ImportSetProperty importSetProperty, Object parentElement )
    {
        String name = importSetProperty.getName();

        Map<String, Object> parentElementAsMap = parentElement instanceof Map ? ( Map<String, Object> ) parentElement : null;
        List parentElementAsList = parentElement instanceof List ? ( List ) parentElement : null;

        switch ( importSetProperty.getType() )
        {
            case "list":
            {
                List list = initializeList( parentElementAsMap, parentElementAsList, name );

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, list );
                }

                break;
            }
            case "object":
            {
                Map<String, Object> embedded = initializeEmbedded( parentElementAsMap, parentElementAsList, name );

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, embedded );
                }

                break;
            }
            default:
            {
                Object convertedValue = converterExecutor.convertProperty( importSetProperty );

                if ( parentElementAsMap != null )
                {
                    parentElementAsMap.put( name, convertedValue );
                }
                if ( parentElementAsList != null )
                {
                    parentElementAsList.add( convertedValue );
                }
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private List initializeList( @Nullable Map<String, Object> parentElementAsMap,
                                 @Nullable List parentElementAsList,
                                 String name )
    {
        List list = null;

        if ( parentElementAsMap != null )
        {
            list = ( List ) parentElementAsMap.get( name );
            if ( list == null )
            {
                list = new ArrayList<>();
                parentElementAsMap.put( name, list );
            }
        }

        if ( parentElementAsList != null )
        {
            list = new ArrayList();
            parentElementAsList.add( list );
        }

        return list;
    }

    @SuppressWarnings( "unchecked" )
    private Map<String, Object> initializeEmbedded( @Nullable Map<String, Object> parentElementAsMap,
                                                    @Nullable List parentElementAsList,
                                                    String name )
    {
        Map<String, Object> embedded = null;

        if ( parentElementAsMap != null )
        {
            embedded = ( Map<String, Object> ) parentElementAsMap.get( name );
            if ( embedded == null )
            {
                embedded = new HashMap<>();
                parentElementAsMap.put( name, embedded );
            }
        }

        if ( parentElementAsList != null )
        {
            embedded = new HashMap<>();
            parentElementAsList.add( embedded );
        }

        return embedded;
    }
}
