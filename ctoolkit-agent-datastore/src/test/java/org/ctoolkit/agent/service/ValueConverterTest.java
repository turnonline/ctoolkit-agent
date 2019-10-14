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

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.LatLng;
import com.google.cloud.datastore.LatLngValue;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.converter.BlobValueResolver;
import org.ctoolkit.agent.converter.BooleanValueResolver;
import org.ctoolkit.agent.converter.DoubleValueResolver;
import org.ctoolkit.agent.converter.EntityValueResolver;
import org.ctoolkit.agent.converter.KeyConverter;
import org.ctoolkit.agent.converter.KeyValueResolver;
import org.ctoolkit.agent.converter.LatLngValueResolver;
import org.ctoolkit.agent.converter.ListValueResolver;
import org.ctoolkit.agent.converter.LongValueResolver;
import org.ctoolkit.agent.converter.StringValueResolver;
import org.ctoolkit.agent.converter.TimestampValueResolver;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.model.RawKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Map;

import static org.ctoolkit.agent.Mocks.date;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class ValueConverterTest
{
    private KeyConverter keyConverter = new KeyConverter( "test" );

    private StringValueResolver stringValueResolver = new StringValueResolver();
    private BooleanValueResolver booleanValueResolver = new BooleanValueResolver();
    private DoubleValueResolver doubleValueResolver = new DoubleValueResolver();
    private LongValueResolver longValueResolver = new LongValueResolver();
    private TimestampValueResolver timestampValueResolver = new TimestampValueResolver();
    private LatLngValueResolver latLngValueResolver = new LatLngValueResolver();
    private BlobValueResolver blobValueResolver = new BlobValueResolver();

    private KeyValueResolver keyValueResolver = new KeyValueResolver( keyConverter );
    private EntityValueResolver entityValueResolver = new EntityValueResolver();
    private ListValueResolver listValueResolver = new ListValueResolver();

    private ValueConverter converter = new ValueConverter( new DatastoreAgentConfig()
            .createValueResolvers(
                    stringValueResolver,
                    booleanValueResolver,
                    doubleValueResolver,
                    longValueResolver,
                    timestampValueResolver,
                    latLngValueResolver,
                    blobValueResolver,

                    keyValueResolver,
                    entityValueResolver,
                    listValueResolver )
    );

    // convert from DB

    @Test
    public void convertFromDbString()
    {
        Map<String, Object> converted = converter.fromValue( "property", new StringValue( "Foo" ) );

        assertEquals( 1, converted.size() );
        assertEquals( "Foo", converted.get( "property" ) );
    }

    @Test
    public void convertFromDbBoolean()
    {
        Map<String, Object> converted = converter.fromValue( "property", new BooleanValue( true ) );

        assertEquals( 1, converted.size() );
        assertTrue( ( Boolean ) converted.get( "property" ) );
    }

    @Test
    public void convertFromDbDouble()
    {
        Map<String, Object> converted = converter.fromValue( "property", new DoubleValue( 100 ) );

        assertEquals( 1, converted.size() );
        assertEquals( 100D, converted.get( "property" ) );
    }

    @Test
    public void convertFromDbLong()
    {
        Map<String, Object> converted = converter.fromValue( "property", new LongValue( 100 ) );

        assertEquals( 1, converted.size() );
        assertEquals( 100L, converted.get( "property" ) );
    }

    @Test
    public void convertFromDbTimestamp()
    {
        Map<String, Object> converted = converter.fromValue( "property", TimestampValue
                .newBuilder( Timestamp.of( date( 8, 4, 2019 ) ) )
                .build()
        );

        assertEquals( 1, converted.size() );
        assertEquals( date( 8, 4, 2019 ), converted.get( "property" ) );
    }

    @Test
    public void convertFromDbLatLng()
    {
        Map<String, Object> converted = converter.fromValue( "property", new LatLngValue( LatLng.of( 12.3, 14.8 ) ) );

        assertEquals( 1, converted.size() );
        assertEquals( 12.3, ( ( org.ctoolkit.agent.model.LatLng ) converted.get( "property" ) ).getLatitude(), 0 );
        assertEquals( 14.8, ( ( org.ctoolkit.agent.model.LatLng ) converted.get( "property" ) ).getLongitude(), 0 );
    }

    @Test
    public void convertFromDbBlob()
    {
        Map<String, Object> converted = converter.fromValue( "property", new BlobValue( Blob.copyFrom( new byte[]{1} ) ) );

        assertEquals( 1, converted.size() );
        assertArrayEquals( new byte[]{1}, ( byte[] ) converted.get( "property" ) );
    }

    @Test
    public void convertFromDbKey()
    {
        Map<String, Object> converted = converter.fromValue( "property", new KeyValue( Key.newBuilder( "test", "Partner", "123" ).build() ) );

        assertEquals( 1, converted.size() );
        assertEquals( new RawKey( "Partner:123" ), converted.get( "property" ) );
    }

    @Test
    public void convertFromDbEntity()
    {
        FullEntity entity = FullEntity.newBuilder()
                .set( "name", new StringValue( "John" ) )
                .set( "surname", new StringValue( "Foo" ) )
                .set( "age", new LongValue( 34 ) )
                .build();

        Map<String, Object> converted = converter.fromValue( "property", new EntityValue( entity ) );

        assertEquals( 3, converted.size() );
        assertEquals( "John", converted.get( "property.name" ) );
        assertEquals( "Foo", converted.get( "property.surname" ) );
        assertEquals( 34L, converted.get( "property.age" ) );
    }

    @Test
    public void convertFromDbEmbeddedEntity()
    {
        FullEntity entityChild = FullEntity.newBuilder()
                .set( "street", new StringValue( "Long street" ) )
                .set( "city", new StringValue( "New york" ) )
                .build();

        FullEntity entity = FullEntity.newBuilder()
                .set( "name", new StringValue( "John" ) )
                .set( "surname", new StringValue( "Foo" ) )
                .set( "age", new LongValue( 34 ) )
                .set( "address", entityChild )
                .build();

        Map<String, Object> converted = converter.fromValue( "property", new EntityValue( entity ) );

        assertEquals( 5, converted.size() );
        assertEquals( "John", converted.get( "property.name" ) );
        assertEquals( "Foo", converted.get( "property.surname" ) );
        assertEquals( 34L, converted.get( "property.age" ) );
        assertEquals( "Long street", converted.get( "property.address.street" ) );
        assertEquals( "New york", converted.get( "property.address.city" ) );
    }

    @Test
    public void convertFromDbList()
    {
        ArrayList<Value<?>> list = new ArrayList<>();
        list.add( new StringValue( "John" ) );
        list.add( new StringValue( "Foo" ) );
        list.add( new LongValue( 34 ) );

        Map<String, Object> converted = converter.fromValue( "property", new ListValue( list ) );

        assertEquals( 3, converted.size() );
        assertEquals( "John", converted.get( "property[0]" ) );
        assertEquals( "Foo", converted.get( "property[1]" ) );
        assertEquals( 34L, converted.get( "property[2]" ) );
    }

    @Test
    public void convertFromDbListWithEntity()
    {
        FullEntity entity = FullEntity.newBuilder()
                .set( "age", new LongValue( 34 ) )
                .build();

        ArrayList<Value<?>> list = new ArrayList<>();
        list.add( new StringValue( "John" ) );
        list.add( new StringValue( "Foo" ) );
        list.add( new EntityValue( entity ) );

        Map<String, Object> converted = converter.fromValue( "property", new ListValue( list ) );

        assertEquals( 3, converted.size() );
        assertEquals( "John", converted.get( "property[0]" ) );
        assertEquals( "Foo", converted.get( "property[1]" ) );
        assertEquals( 34L, converted.get( "property[2].age" ) );
    }

    @Test
    public void convertFromDbEntityWithList()
    {
        ArrayList<Value<?>> list = new ArrayList<>();
        list.add( new StringValue( "KLO" ) );
        list.add( new StringValue( "BAB" ) );

        FullEntity entity = FullEntity.newBuilder()
                .set( "name", new StringValue( "John" ) )
                .set( "surname", new StringValue( "Foo" ) )
                .set( "codes", new ListValue( list ) )
                .build();

        Map<String, Object> converted = converter.fromValue( "property", new EntityValue( entity ) );

        assertEquals( 4, converted.size() );
        assertEquals( "John", converted.get( "property.name" ) );
        assertEquals( "Foo", converted.get( "property.surname" ) );
        assertEquals( "KLO", converted.get( "property.codes[0]" ) );
        assertEquals( "BAB", converted.get( "property.codes[1]" ) );
    }
}