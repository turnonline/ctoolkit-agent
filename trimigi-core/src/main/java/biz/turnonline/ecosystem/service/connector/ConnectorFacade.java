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

import com.google.common.annotations.VisibleForTesting;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Connector facade acts as a bridge between various implementations of {@link Connector} api.
 * Selection of correct {@link Connector} is based on connectionString prefix. For instance if
 * connection string starts with http://, {@link RestConnector} implementation will be used
 * to execute pulling/pushing data.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ConnectorFacade
        implements Connector
{
    private static final String NO_CONNECTOR_MSG = "No connector is implemented for connection string: ";

    private Map<String, Connector> connectors = new HashMap<>();

    @Inject
    @Named( "rest" )
    private Connector restConnector;

    @Inject
    @Named( "kafka" )
    private Connector kafkaConnector;

    @Override
    public void push( String connectionString, Object payload )
    {
        select( connectionString ).push( connectionString, payload );
    }

    @Override
    public Object pull( String connectionString, Map<String, String> queryParams )
    {
        return select( connectionString ).pull( connectionString, queryParams );
    }

    @PostConstruct
    @VisibleForTesting
    protected void init()
    {
        connectors.put( "http://", restConnector );
        connectors.put( "https://", restConnector );
        connectors.put( "kafka://", kafkaConnector );
    }

    // private helpers

    private Connector select( String connectionString )
    {
        String prefix = connectors.keySet()
                .stream()
                .filter( connectionString::startsWith )
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException( NO_CONNECTOR_MSG + connectionString ) );

        return connectors.get( prefix );
    }
}
