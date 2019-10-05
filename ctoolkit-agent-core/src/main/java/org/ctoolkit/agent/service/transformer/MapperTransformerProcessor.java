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

package org.ctoolkit.agent.service.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformerMappings;

import java.util.Map;

/**
 * Implementation of {@link MigrationSetPropertyMapperTransformer} transformer
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MapperTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyMapperTransformer>
{
    @Override
    public Object transform( Object value, MigrationSetPropertyMapperTransformer transformer, Map<Object, Object> ctx )
    {
        for ( MigrationSetPropertyMapperTransformerMappings mapping : transformer.getMappings() )
        {
            if ( mapping.getSource().equals( value ) )
            {
                return mapping.getTarget();
            }
        }

        return value;
    }
}
