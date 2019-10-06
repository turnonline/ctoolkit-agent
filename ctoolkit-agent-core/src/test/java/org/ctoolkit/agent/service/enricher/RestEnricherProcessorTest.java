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

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.ctoolkit.agent.Mocks;
import org.ctoolkit.agent.model.api.MigrationSetRestEnricher;
import org.ctoolkit.agent.model.api.QueryParameter;
import org.ctoolkit.agent.service.connector.ConnectorFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.io.InputStreamReader;
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
public class RestEnricherProcessorTest
{
    @Mock
    private ConnectorFacade connectorFacade;

    @InjectMocks
    private RestEnricherProcessor processor = new RestEnricherProcessor();

    @Test
    @SuppressWarnings( "unchecked" )
    public void enrich() throws Exception
    {
        InputStream json = RestEnricherProcessorTest.class.getResourceAsStream( "/rest-enricher.json" );
        String response = CharStreams.toString( new InputStreamReader( json, Charsets.UTF_8 ) );

        doAnswer( new Answer()
        {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable
            {
                String url = ( String ) invocation.getArguments()[0];
                Map<String, String> queryParams = ( Map<String, String> ) invocation.getArguments()[1];

                assertEquals( "http://foo.com", url );
                assertEquals( "123", queryParams.get( "id" ) );
                assertEquals( "2019-02-01", queryParams.get( "birthDate" ) );
                assertEquals( "2017-01-10T01:02:03.000+01", queryParams.get( "createDate" ) );

                return response;
            }
        } ).when( connectorFacade ).pull( anyString(), anyMap() );

        MigrationSetRestEnricher enricher = new MigrationSetRestEnricher();
        enricher.setUrl( "http://foo.com" );
        enricher.setName( "person" );

        QueryParameter id = new QueryParameter();
        id.setName( "id" );
        id.setValue( "identifier" );
        enricher.getQueryParameters().add( id );

        QueryParameter birthDate = new QueryParameter();
        birthDate.setName( "birthDate" );
        birthDate.setValue( "birthDat" );
        birthDate.setConverter( "date" );
        enricher.getQueryParameters().add( birthDate );

        QueryParameter createDate = new QueryParameter();
        createDate.setName( "createDate" );
        createDate.setValue( "creatDat" );
        createDate.setConverter( "datetime" );
        enricher.getQueryParameters().add( createDate );

        Map<String, Object> ctx = new HashMap<>();
        ctx.put( "identifier", "123" );
        ctx.put( "birthDat", Mocks.date( 1, 2, 2019 ) );
        ctx.put( "creatDat", Mocks.date( 10, 1, 2017, 1, 2, 3 ) );
        processor.enrich( enricher, ctx );

        assertEquals( "KLO", ctx.get( "person.codes[0]" ) );
        assertEquals( "BLI", ctx.get( "person.codes[1]" ) );
        assertEquals( "Long street", ctx.get( "person.address.street" ) );
        assertEquals( "John", ctx.get( "person.name" ) );
        assertEquals( "Foo", ctx.get( "person.surname" ) );
        assertEquals( "1234567890", ctx.get( "person.identifications[0].identification.value" ) );
        assertEquals( "BIRTH_NUMBER", ctx.get( "person.identifications[0].identification.type" ) );
    }
}