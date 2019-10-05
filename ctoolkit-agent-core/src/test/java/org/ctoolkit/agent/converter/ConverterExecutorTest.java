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

import org.ctoolkit.agent.Mocks;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.converter.ConverterRegistrat;
import org.ctoolkit.agent.service.transformer.TransformerExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for {@link ConverterExecutor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class ConverterExecutorTest
{
    @Mock
    private ConverterRegistrat registrat;

    private ConverterExecutor executor = new ConverterExecutor( new TransformerExecutor(), registrat );

    // -- convert id

    @Test
    public void testConvertId_IdSelectorNull()
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setIdSelector( null );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( source );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        String converted = executor.convertId( migrationSet, Mocks.migrationContext( "id", 1L ) );
        assertNull( converted );
    }

    @Test
    public void testConvertId_Plain()
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setIdSelector( "${target.namespace}:${target.kind}:${id}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( source );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        String converted = executor.convertId( migrationSet, Mocks.migrationContext( "id", 1L ) );
        assertEquals( "client-person:person:1", converted );
    }

    @Test
    public void testConvertId_EncodeBase64()
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setIdSelector( "encode:${target.namespace}:${target.kind}:${id}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( source );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        String converted = executor.convertId( migrationSet, Mocks.migrationContext( "id", 1L ) );
        assertEquals( "Y2xpZW50LXBlcnNvbjpwZXJzb246MQ==", converted );
    }
}