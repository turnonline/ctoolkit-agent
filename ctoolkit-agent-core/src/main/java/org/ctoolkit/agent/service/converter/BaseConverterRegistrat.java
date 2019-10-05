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

package org.ctoolkit.agent.service.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter registrat base class
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class BaseConverterRegistrat
        implements ConverterRegistrat
{
    private Map<Converter.Key, Converter> converters = new HashMap<>();

    public BaseConverterRegistrat()
    {
        initialize();
    }

    public abstract void initialize();

    @Override
    public void register( Class source, String target, Converter converter )
    {
        converters.put( converter.key( source, target ), converter );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Converter get( Object sourceValue, String targetTypeName )
    {
        for ( Map.Entry<Converter.Key, Converter> entry : converters.entrySet() )
        {
            Class sourceClassName = entry.getKey().getSourceClassName();
            String targetType = entry.getKey().getTargetTypeName();

            if ( sourceClassName.isAssignableFrom( sourceValue.getClass() ) && targetTypeName.equals( targetType ) )
            {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public Converter get( String targetTypeName )
    {
        for ( Map.Entry<Converter.Key, Converter> entry : converters.entrySet() )
        {
            String targetType = entry.getKey().getTargetTypeName();

            if ( targetTypeName.equals( targetType ) )
            {
                return entry.getValue();
            }
        }

        return null;
    }
}
