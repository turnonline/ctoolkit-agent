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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.base.Preconditions;
import com.google.datastore.v1.client.Datastore;
import com.google.datastore.v1.client.DatastoreFactory;
import com.google.datastore.v1.client.DatastoreOptions;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
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
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.converter.ValueResolver;
import org.ctoolkit.agent.mapper.ImportSetPropertyToEntityMapper;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
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
    @Named( "projectId" )
    public String createProjectId()
    {
        String projectId = System.getenv( GOOGLE_CLOUD_PROJECT );
        Preconditions.checkNotNull( projectId, "Environment property '" + GOOGLE_CLOUD_PROJECT + "' not set!" );
        return projectId;
    }

    @Bean
    @Singleton
    public com.google.cloud.datastore.Datastore createDatastore( @Named( "projectId" ) String projectId )
    {
        return com.google.cloud.datastore.DatastoreOptions.getDefaultInstance().getService();
    }

    @Bean
    @Singleton
    public Datastore createPbDatastore( @Named( "projectId" ) String projectId )
    {
        GoogleCredential credential;
        try
        {
            credential = GoogleCredential.getApplicationDefault();
        }
        catch ( IOException e )
        {
            throw new IllegalStateException( "Unable to load google credentials", e );
        }

        DatastoreOptions options = new DatastoreOptions.Builder()
                .projectId( projectId )
                .credential( credential )
                .build();

        return DatastoreFactory.get().create( options );
    }

    @Bean
    @Singleton
    public ConverterExecutor createConverterExecutor()
    {
        return new ConverterExecutor( new DatastoreConverterRegistrat() );
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

    @Bean
    @Singleton
    public ImportSetPropertyToEntityMapper createImportSetPropertyMapper( ConverterExecutor converterExecutor, ValueConverter valueConverter )
    {
        return new ImportSetPropertyToEntityMapper( converterExecutor, valueConverter );
    }
}
