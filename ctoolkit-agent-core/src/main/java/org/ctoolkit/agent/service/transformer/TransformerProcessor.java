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

import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;

import java.util.Map;

/**
 * Transformer processor is used to implement {@link MigrationSetPropertyTransformer} transform logic
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface TransformerProcessor<TRANSFORMER extends MigrationSetPropertyTransformer>
{
    enum Phase
    {
        PRE_CONVERT( "pre-convert" ),
        POST_CONVERT( "post-convert" );

        private String value;

        Phase( String value )
        {
            this.value = value;
        }

        public static Phase get( String value )
        {
            for ( Phase phase : Phase.values() )
            {
                if ( value.equals( phase.value() ) )
                {
                    return phase;
                }
            }

            throw new IllegalArgumentException( "Phase '" + value + "' not supported. Supported phases are: 'pre-convert', 'post-convert'" );
        }

        public String value()
        {
            return value;
        }
    }

    Object transform( Object value, TRANSFORMER transformer, Map<String, Object> ctx );
}
