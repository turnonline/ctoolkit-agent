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

import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyEncodingTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformer executor is used to apply {@link MigrationSetPropertyTransformer} via {@link TransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class TransformerExecutor
{
    private static Map<Class, TransformerProcessor> transformerProcessors = new HashMap<>();

    static
    {
        transformerProcessors.put( MigrationSetPropertyMapperTransformer.class, new MapperTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyDateTransformer.class, new DateTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyBlobTransformer.class, new BlobTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyPatternTransformer.class, new PatternTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyEncodingTransformer.class, new EncodingTransformerProcessor() );
    }

    @SuppressWarnings( "unchecked" )
    public <T> T transform( Object value, List<MigrationSetPropertyTransformer> transformers, Map<Object, Object> ctx, String phase )
    {
        if ( transformers != null )
        {

            for ( MigrationSetPropertyTransformer transformer : transformers )
            {
                if ( transformer.getPhase().equals( phase ) )
                {
                    TransformerProcessor processor = transformerProcessors.get( transformer.getClass() );
                    if ( processor != null )
                    {
                        value = processor.transform( value, transformer, ctx );
                    }
                }
            }
        }

        return ( T ) value;
    }
}
