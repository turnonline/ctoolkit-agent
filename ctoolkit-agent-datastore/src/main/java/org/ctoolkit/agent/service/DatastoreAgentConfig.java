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

package org.ctoolkit.agent.service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.common.base.Preconditions;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.commons.lang.NotImplementedException;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.converter.BlobValueResolver;
import org.ctoolkit.agent.converter.BooleanValueResolver;
import org.ctoolkit.agent.converter.DoubleValueResolver;
import org.ctoolkit.agent.converter.EntityValueResolver;
import org.ctoolkit.agent.converter.KeyValueResolver;
import org.ctoolkit.agent.converter.LatLngValueResolver;
import org.ctoolkit.agent.converter.ListValueResolver;
import org.ctoolkit.agent.converter.LongValueResolver;
import org.ctoolkit.agent.converter.StringValueResolver;
import org.ctoolkit.agent.converter.TimestampValueResolver;
import org.ctoolkit.agent.converter.ValueResolver;
import org.ctoolkit.agent.service.converter.ConverterExecutor;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Mongo configuration
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class DatastoreAgentConfig
{
    // https://cloud.google.com/appengine/docs/flexible/java/migrating
    private static final String GOOGLE_CLOUD_PROJECT = "GOOGLE_CLOUD_PROJECT";

    @Bean
    @Singleton
    @ProjectId
    public String createProjectId()
    {
        String projectId = System.getenv( GOOGLE_CLOUD_PROJECT );
        Preconditions.checkNotNull( projectId, "Environment property '" + GOOGLE_CLOUD_PROJECT + "' not set!" );
        return projectId;
    }

    @Bean
    @Singleton
    public Datastore createDatastore()
    {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Bean
    @Singleton
    public ConverterExecutor createConverterExecutor()
    {
        throw new NotImplementedException( "Provide createConverterExecutor" );
    }

    @Bean
    @Singleton
    public List<ValueResolver> createValueResolvers( StringValueResolver stringValueResolver,
                                                     BooleanValueResolver booleanValueResolver,
                                                     DoubleValueResolver doubleValueResolver,
                                                     LongValueResolver longValueResolver,
                                                     TimestampValueResolver timestampValueResolver,
                                                     LatLngValueResolver latLngValueResolver,
                                                     BlobValueResolver blobValueResolver,

                                                     KeyValueResolver keyValueResolver,
                                                     EntityValueResolver entityValueResolver,
                                                     ListValueResolver listValueResolver )
    {
        List<ValueResolver> resolvers = new ArrayList<>();

        // simple types
        resolvers.add( stringValueResolver );
        resolvers.add( booleanValueResolver );
        resolvers.add( doubleValueResolver );
        resolvers.add( longValueResolver );
        resolvers.add( timestampValueResolver );
        resolvers.add( latLngValueResolver );
        resolvers.add( blobValueResolver );

        // complex types
        resolvers.add( keyValueResolver );
        resolvers.add( entityValueResolver );
        resolvers.add( listValueResolver );

        return resolvers;
    }
}
