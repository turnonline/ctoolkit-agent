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

package org.ctoolkit.agent.service.connector;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.DefaultHttpClient;
import io.micronaut.http.client.RxHttpClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Rest implementation of {@link Connector}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
@Named( "rest" )
public class RestConnector
        implements Connector
{
    @Inject
    private RxHttpClient httpClient;

    @Override
    @SuppressWarnings( "unchecked" )
    public void push( String connectionString, Object payload )
    {
        MutableHttpRequest post = HttpRequest.POST( connectionString, payload );
        httpClient.retrieve( post ).blockingSubscribe();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object pull( String connectionString, Map<String, String> queryParams )
    {
        try
        {
            RxHttpClient httpClient = new DefaultHttpClient( new URL( connectionString ) );
            MutableHttpRequest get = HttpRequest.GET( "" );
            return httpClient.retrieve( get ).blockingFirst();
        }
        catch ( MalformedURLException e )
        {
            throw new IllegalArgumentException( "Unable to construct migration client", e );
        }
    }
}