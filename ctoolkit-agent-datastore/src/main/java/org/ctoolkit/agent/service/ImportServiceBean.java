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
import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.ProjectionEntity;
import com.google.cloud.datastore.ProjectionEntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.converter.KeyConverter;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.service.converter.ConverterExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ImportService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ImportServiceBean
        implements ImportService
{
    @Inject
    private Datastore datastore;

    @Inject
    private ConverterExecutor converterExecutor;

    @Inject
    private KeyConverter keyConverter;

    @Inject
    private ValueConverter valueConverter;

    @Override
    public void importData( ImportSet importSet )
    {
        // delete collection if requested
        if ( "DELETE".equals( importSet.getClean() ) )
        {
            deleteCollection( importSet );
        }

        // import if namespace and kind are specified
        if ( importSet.getNamespace() != null && importSet.getKind() != null )
        {
            insertRecord( importSet );
        }
    }

    // -- private helpers

    private void insertRecord( ImportSet importSet )
    {
        Key key = keyConverter.convertFromRawKey( importSet.getId() );
        FullEntity.Builder rootEntity = FullEntity.newBuilder( key );

        for ( ImportSetProperty property : importSet.getProperties() )
        {
            addProperty( property, rootEntity );
        }

        datastore.put( rootEntity.build() );
    }

    // -- private helpers

    @SuppressWarnings( "unchecked" )
    private <PARENT> void addProperty( ImportSetProperty importSetProperty, PARENT parentElement )
    {
        Value<?> value;

        FullEntity.Builder parentElementAsEntity = parentElement instanceof FullEntity.Builder ? ( FullEntity.Builder ) parentElement : null;
        List<Value<?>> parentElementAsList = parentElement instanceof List ? ( List<Value<?>> ) parentElement : null;

        switch ( importSetProperty.getType() )
        {
            case "list":
            {
                List<Value<?>> list = new ArrayList<>();

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, list );
                }

                value = ListValue.newBuilder().set( list ).build();

                break;
            }
            case "object":
            {
                FullEntity.Builder embedded = FullEntity.newBuilder();

                List<ImportSetProperty> properties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                for ( ImportSetProperty property : properties )
                {
                    addProperty( property, embedded );
                }

                value = EntityValue.of( embedded.build() );

                break;
            }
            default:
            {
                Object convertedValueObject = converterExecutor.convertProperty( importSetProperty );
                value = valueConverter.toValue( convertedValueObject );
            }
        }

        if ( parentElementAsEntity != null )
        {
            parentElementAsEntity.set( importSetProperty.getName(), value );
        }
        if ( parentElementAsList != null )
        {
            parentElementAsList.add( value );
        }
    }

    private void deleteCollection( ImportSet importSet )
    {
        boolean process = true;

        while ( process )
        {
            ProjectionEntityQuery.Builder deleteQuery = Query.newProjectionEntityQueryBuilder();
            deleteQuery.setKind( importSet.getKind() );
            deleteQuery.addProjection( "__key__" );
            deleteQuery.setLimit( importSet.getCleanBatchItemsLimit().intValue() );

            QueryResults<ProjectionEntity> results = datastore.run( deleteQuery.build() );
            process = results.hasNext();

            List<Key> keysToDelete = new ArrayList<>();
            results.forEachRemaining( projectionEntity -> keysToDelete.add( projectionEntity.getKey() ) );

            datastore.delete( keysToDelete.toArray( new Key[]{} ) );
        }
    }
}
