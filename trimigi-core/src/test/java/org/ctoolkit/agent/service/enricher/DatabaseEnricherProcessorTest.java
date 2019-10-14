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

package org.ctoolkit.agent.service.enricher;

import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.MigrationSetDatabaseSelectEnricher;
import org.ctoolkit.agent.model.api.NamedParameter;
import org.ctoolkit.agent.service.ExportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class DatabaseEnricherProcessorTest
{
    @Mock
    private ExportService exportService;

    @InjectMocks
    private DatabaseEnricherProcessor processor = new DatabaseEnricherProcessor();

    @Test
    @SuppressWarnings( "unchecked" )
    public void enrich()
    {
        MigrationSetDatabaseSelectEnricher enricher = new MigrationSetDatabaseSelectEnricher();
        enricher.setQuery( "select * from person where person_id=:id" );
        enricher.setName( "person" );

        NamedParameter namedParameter = new NamedParameter();
        namedParameter.setName( "id" );
        namedParameter.setValue( "${identifier}" );
        enricher.getNamedParameters().add( namedParameter );

        Map<String, Object> ctx = new HashMap<>();
        ctx.put( "identifier", "109" );

        doAnswer( invocation -> {
            String query = ( String ) invocation.getArguments()[0];
            Map<String, Object> namedParameters = ( Map<String, Object> ) invocation.getArguments()[1];

            assertEquals( "select * from person where person_id=:id", query );
            assertEquals( "109", namedParameters.get( "id" ) );

            Export row1 = new Export();
            row1.put( "name", "John" );
            row1.put( "surname", "Foo" );

            Export row2 = new Export();
            row2.put( "name", "Bill" );
            row2.put( "surname", "Bilbo" );

            return Arrays.asList( row1, row2 );
        } ).when( exportService ).executeQuery( anyString(), anyMap() );

        processor.enrich( enricher, ctx );

        assertEquals( "John", ctx.get( "person.row[0].name" ) );
        assertEquals( "Foo", ctx.get( "person.row[0].surname" ) );
        assertEquals( "Bill", ctx.get( "person.row[1].name" ) );
        assertEquals( "Bilbo", ctx.get( "person.row[1].surname" ) );
    }
}