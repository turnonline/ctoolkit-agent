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

package org.ctoolkit.agent.converter;

import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.Value;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GCP datastore value converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ValueConverter
{
    private final List<ValueResolver> resolvers;

    @Inject
    public ValueConverter( List<ValueResolver> resolvers )
    {
        this.resolvers = resolvers;
    }

    @SuppressWarnings( "unchecked" )
    public Map<String, Object> fromValue( String name, Value<?> value )
    {
        Map<String, Object> valueMap = new HashMap<>();

        ValueResolver resolver = resolvers.stream()
                .filter( r -> r.applyValue( value ) )
                .findFirst()
                .orElse( null );

        if ( resolver != null )
        {
            if ( resolver instanceof EntityValueResolver || resolver instanceof ListValueResolver )
            {
                Map<String, Value<?>> resolved = resolver.fromValue( name, value );
                resolved.forEach( ( embeddedName, val ) -> valueMap.putAll( fromValue( embeddedName, val ) ) );
            }
            else
            {
                valueMap.putAll( resolver.fromValue( name, value ) );
            }
        }

        return valueMap;
    }

    @SuppressWarnings( "unchecked" )
    public Value<?> toValue( Object object )
    {
        ValueResolver valueResolver = resolvers.stream().filter( resolver -> {
            return resolver.applyObject( object );
        } ).findFirst().orElse( null );

        if (valueResolver != null) {
            return valueResolver.toValue( object );
        }

        return NullValue.of();
    }
}
