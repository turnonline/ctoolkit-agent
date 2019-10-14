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

package biz.turnonline.ecosystem.service.mapper;

import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.service.converter.ConverterExecutor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ImportSetProperty} to Map structure mapper. This mapper works for every target database
 * which structure is created via Map
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportSetPropertyToMapMapper
        implements Mapper<ImportSetProperty, Object>
{
    private final ConverterExecutor converterExecutor;

    public ImportSetPropertyToMapMapper( ConverterExecutor converterExecutor )
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
        Object value;

        switch ( importSetProperty.getType() )
        {
            case "list":
            {
                value = new ArrayList(  );

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, value );
                }

                break;
            }
            case "object":
            {
                value = new LinkedHashMap<>(  );

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, value );
                }

                break;
            }
            default:
            {
                value = converterExecutor.convertProperty( importSetProperty );
            }
        }

        Map<String, Object> parentElementAsMap = parentElement instanceof Map ? ( Map<String, Object> ) parentElement : null;
        List parentElementAsList = parentElement instanceof List ? ( List ) parentElement : null;

        if ( parentElementAsMap != null )
        {
            parentElementAsMap.put( importSetProperty.getName(), value );
        }
        if ( parentElementAsList != null )
        {
            parentElementAsList.add( value );
        }
    }
}
