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

import org.ctoolkit.agent.Mocks;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link PatternTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class PatternTransformerProcessorTest
{
    private PatternTransformerProcessor processor = new PatternTransformerProcessor();

    @Test
    public void transform_WithPrefix()
    {
        MigrationSetPropertyPatternTransformer transformer = new MigrationSetPropertyPatternTransformer();
        transformer.setPattern( "client-person:person:{target.value}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( Mocks.migrationSetSource( "global", "Person" ) );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        HashMap<Object, Object> ctx = new HashMap<>();
        ctx.put( MigrationSet.class, migrationSet );

        assertEquals( "client-person:person:1", processor.transform( "1", transformer, ctx ) );
    }

    @Test
    public void transform_WithContext()
    {
        MigrationSetPropertyPatternTransformer transformer = new MigrationSetPropertyPatternTransformer();
        transformer.setPattern( "{target.namespace}:{target.kind}:{target.value}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( Mocks.migrationSetSource( "global", "Person" ) );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        HashMap<Object, Object> ctx = new HashMap<>();
        ctx.put( MigrationSet.class, migrationSet );

        assertEquals( "client-person:person:1", processor.transform( "1", transformer, ctx ) );
    }
}