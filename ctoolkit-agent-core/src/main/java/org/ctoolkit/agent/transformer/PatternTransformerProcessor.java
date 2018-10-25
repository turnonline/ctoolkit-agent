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

package org.ctoolkit.agent.transformer;

import org.apache.commons.text.StringSubstitutor;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MigrationSetPropertyPatternTransformer} transformer
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class PatternTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyPatternTransformer>
{
    @Override
    public Object transform( Object value, MigrationSetPropertyPatternTransformer transformer, Map<Object, Object> ctx )
    {
        if ( value instanceof String )
        {
            MigrationSet migrationSet = (MigrationSet) ctx.get( MigrationSet.class );

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put( "source.namespace", migrationSet.getSource().getNamespace() );
            placeholders.put( "source.kind", migrationSet.getSource().getKind() );

            placeholders.put( "target.namespace", migrationSet.getTarget().getNamespace() );
            placeholders.put( "target.kind", migrationSet.getTarget().getKind() );
            placeholders.put( "target.value", ( String ) value );

            StringSubstitutor substitution = new StringSubstitutor( placeholders, "{", "}" );
            value = substitution.replace( transformer.getPattern() );
        }

        return value;
    }
}
