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

/**
 * Converter registrat API
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ConverterRegistrat
{
    /**
     * Register converter
     *
     * @param source source class
     * @param target target value
     * @param converter converter to use
     */
    void register( Class source, String target, Converter converter );

    /**
     * Get converter by source value and target type name
     *
     * @param sourceValue source value
     * @param targetTypeName target type name (i.e. text)
     * @return converter or <code>null</code> if converter could not be found for sourceValue/targetTypeName combination
     */
    Converter get( Object sourceValue, String targetTypeName );

    /**
     * Get converter by target type name
     *
     * @param targetTypeName target type name (i.e. text)
     * @return converter or <code>null</code> if converter could not be found for targetTypeName
     */
    Converter get(String targetTypeName);
}
