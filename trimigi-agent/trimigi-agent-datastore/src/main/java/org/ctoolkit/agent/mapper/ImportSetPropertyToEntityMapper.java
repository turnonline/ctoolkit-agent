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

package org.ctoolkit.agent.mapper;

import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ImportSetProperty} to {@link FullEntity.Builder} structure mapper.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportSetPropertyToEntityMapper
        implements Mapper<ImportSetProperty, FullEntity.Builder>
{
    private final ConverterExecutor converterExecutor;

    private final ValueConverter valueConverter;

    public ImportSetPropertyToEntityMapper( ConverterExecutor converterExecutor, ValueConverter valueConverter )
    {
        this.converterExecutor = converterExecutor;
        this.valueConverter = valueConverter;
    }

    @Override
    public void map( ImportSetProperty source, FullEntity.Builder target )
    {
        addProperty( source, target );
    }

    @SuppressWarnings( "unchecked" )
    private void addProperty( ImportSetProperty importSetProperty, Object parentElement )
    {
        Value<?> value;

        switch ( importSetProperty.getType() )
        {
            case "list":
            {
                List<Value<?>> list = new ArrayList<>();

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, list );
                }

                value = ListValue.newBuilder().set( list ).build();

                break;
            }
            case "object":
            {
                FullEntity.Builder embedded = FullEntity.newBuilder();

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, embedded );
                }

                value = EntityValue.of( embedded.build() );

                break;
            }
            default:
            {
                Object convertedValueObject = converterExecutor.convertProperty( importSetProperty );
                value = valueConverter.toValue( convertedValueObject );
            }
        }

        FullEntity.Builder parentElementAsEntity = parentElement instanceof FullEntity.Builder ? ( FullEntity.Builder ) parentElement : null;
        List<Value<?>> parentElementAsList = parentElement instanceof List ? ( List<Value<?>> ) parentElement : null;

        if ( parentElementAsEntity != null )
        {
            parentElementAsEntity.set( importSetProperty.getName(), value );
        }
        if ( parentElementAsList != null )
        {
            parentElementAsList.add( value );
        }
    }


}
