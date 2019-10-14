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

package biz.turnonline.ecosystem.converter;

import com.google.cloud.datastore.Value;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * GCP datastore value converter. Each data type is represented by custom resolver.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ValueResolver<T, V extends Value<?>>
        extends Serializable
{
    Map<String, T> fromValue( String name, V value );

    V toValue( T object );

    default boolean applyValue( Value<?> value )
    {
        Class valueClass = ( Class ) ( ( ParameterizedTypeImpl ) getClass().getGenericInterfaces()[0] ).getActualTypeArguments()[1];
        return value.getClass().equals( valueClass );
    }

    default boolean applyObject( T object )
    {
        try
        {
            Type typeArguments = ( ( ParameterizedTypeImpl ) getClass().getGenericInterfaces()[0] ).getActualTypeArguments()[0];
            if (typeArguments instanceof Class)
            {
                Class objectClass = ( Class ) typeArguments;
                return object.getClass().equals( objectClass );
            }

            return false;
        }
        catch ( Exception e )
        {
            System.out.println( "XXX: " + getClass().getGenericInterfaces()[0] ); // TODO: fix exception for ListValueResolver
            return false;
        }
    }
}
