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

package biz.turnonline.ecosystem.service.connector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit test for {@link ConnectorFacade}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class ConnectorFacadeTest
{
    @Mock
    private Connector restConnector;

    @Mock
    private Connector kafkaConnector;

    @InjectMocks
    private ConnectorFacade facade = new ConnectorFacade();

    @Before
    public void setUp() throws Exception
    {
        facade.init();
    }

    // -- push

    @Test( expected = IllegalArgumentException.class )
    public void push_UnknownConnector()
    {
        try
        {
            facade.push( "loop://google.com", null );
        }
        catch ( IllegalArgumentException e )
        {
            assertEquals( "No connector is implemented for connection string: loop://google.com", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void push_Rest_NonSecure()
    {
        facade.push( "http://google.com", null );

        verifyZeroInteractions( kafkaConnector );
        verify( restConnector ).push( "http://google.com", null );
    }

    @Test
    public void push_Rest_Secure()
    {
        facade.push( "https://google.com", null );

        verifyZeroInteractions( kafkaConnector );
        verify( restConnector ).push( "https://google.com", null );
    }

    @Test
    public void push_Kafka()
    {
        facade.push( "kafka://kafka:9092/contact_topic", null );

        verifyZeroInteractions( restConnector );
        verify( kafkaConnector ).push( "kafka://kafka:9092/contact_topic", null );
    }

    // -- pull

    @Test( expected = IllegalArgumentException.class )
    public void pull_UnknownConnector()
    {
        try
        {
            facade.pull( "loop://google.com", null );
        }
        catch ( IllegalArgumentException e )
        {
            assertEquals( "No connector is implemented for connection string: loop://google.com", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void pull_Rest_NonSecure()
    {
        facade.pull( "http://google.com", null );

        verifyZeroInteractions( kafkaConnector );
        verify( restConnector ).pull( "http://google.com", null );
    }

    @Test
    public void pull_Rest_Secure()
    {
        facade.pull( "https://google.com", null );

        verifyZeroInteractions( kafkaConnector );
        verify( restConnector ).pull( "https://google.com", null );
    }

    @Test
    public void pull_Kafka()
    {
        facade.pull( "kafka://kafka:9092/contact_topic", null );

        verifyZeroInteractions( restConnector );
        verify( kafkaConnector ).pull( "kafka://kafka:9092/contact_topic", null );
    }
}