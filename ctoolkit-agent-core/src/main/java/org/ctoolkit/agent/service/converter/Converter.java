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

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;

import java.util.Objects;

/**
 * API for converters
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface Converter
{
    /**
     * Convert source value to target value using {@link MigrationSetProperty}
     *
     * @param source   source exported value
     * @param property {@link MigrationSetProperty}
     * @return converted string value
     */
    String convert( Object source, MigrationSetProperty property );

    /**
     * Convert target value into object using {@link ImportSetProperty}
     * @param property {@link ImportSetProperty}
     * @return converted object value
     */
    Object convert( ImportSetProperty property);

    /**
     * Provide converter key used in {@link BaseConverterRegistrat#register(Class, String, Converter)} method
     *
     * @param source source exported value
     * @param target target property string value, i.e. 'text'
     * @return {@link Key}
     */
    default Key key( Class source, String target )
    {
        return new Key( source, target );
    }

    /**
     * Converter key used in {@link BaseConverterRegistrat#register(Class, String, Converter)}
     */
    class Key
    {
        private Class sourceClassName;

        private String targetTypeName;

        public Key( Class sourceClassName, String targetTypeName )
        {
            this.sourceClassName = sourceClassName;
            this.targetTypeName = targetTypeName;
        }

        public Class getSourceClassName()
        {
            return sourceClassName;
        }

        public String getTargetTypeName()
        {
            return targetTypeName;
        }

        @Override
        public boolean equals( Object o )
        {
            if ( this == o ) return true;
            if ( !( o instanceof Key ) ) return false;
            Key key = ( Key ) o;
            return Objects.equals( sourceClassName, key.sourceClassName.asSubclass( sourceClassName )) &&
                    Objects.equals( targetTypeName, key.targetTypeName );
        }

        @Override
        public int hashCode()
        {
            return Objects.hash( sourceClassName, targetTypeName );
        }

        @Override
        public String toString()
        {
            return "ConverterKey{" +
                    "sourceClassName='" + sourceClassName + '\'' +
                    ", targetTypeName='" + targetTypeName + '\'' +
                    '}';
        }
    }

}
