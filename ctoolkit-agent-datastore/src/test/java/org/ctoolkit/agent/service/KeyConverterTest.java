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

import com.google.cloud.datastore.Key;
import org.ctoolkit.agent.converter.KeyConverter;
import org.junit.Test;

import java.util.List;

import static org.ctoolkit.agent.Mocks.keyConverter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class KeyConverterTest
{
    private KeyConverter converter = keyConverter();

    // -- Key to raw key

    @Test
    public void convertKeyToString_SimpleId()
    {
        Key key = Key.newBuilder( "test", "Partner", 1 ).build();
        assertEquals( "Partner:1", converter.convertFromKey( key ) );
    }

    @Test
    public void convertKeyToString_SimpleName()
    {
        Key key = Key.newBuilder( "test", "Country", "SK" ).build();
        assertEquals( "Country:SK", converter.convertFromKey( key ) );
    }

    @Test
    public void convertKeyToString_AncestorId()
    {
        Key keyParent = Key.newBuilder( "test", "Partner", 1 ).build();
        Key key = Key.newBuilder( keyParent, "Address", 10 ).build();
        assertEquals( "Partner:1>Address:10", converter.convertFromKey( key ) );
    }

    @Test
    public void convertKeyToString_AncestorName()
    {
        Key keyParent = Key.newBuilder( "test", "Country", "SK" ).build();
        Key key = Key.newBuilder( keyParent, "Region", "BA" ).build();
        assertEquals( "Country:SK>Region:BA", converter.convertFromKey( key ) );
    }

    @Test
    public void convertKeyToString_AncestorNameAndId()
    {
        Key keyParentParent = Key.newBuilder( "test", "Partner", 1 ).build();
        Key keyParent = Key.newBuilder( keyParentParent, "Address", 10 ).build();
        Key key = Key.newBuilder( keyParent, "Country", "SK" ).build();
        assertEquals( "Partner:1>Address:10>Country:SK", converter.convertFromKey( key ) );
    }

    // -- raw key to Key

    @Test
    public void convertStringToKey_SimpleId()
    {
        Key key = converter.convertFromRawKey( "Partner:1" );

        assertEquals( "Partner", key.getKind() );
        assertEquals( Long.valueOf( 1 ), key.getId() );
        assertNull( key.getName() );
    }

    @Test
    public void convertStringToKey_SimpleName()
    {
        Key key = converter.convertFromRawKey( "Country:SK" );

        assertEquals( "Country", key.getKind() );
        assertNull( key.getId() );
        assertEquals( "SK", key.getName() );
    }

    @Test
    public void convertStringToKey_AncestorId()
    {
        Key key = converter.convertFromRawKey( "Partner:1>Address:10" );

        assertEquals( "Address", key.getKind() );
        assertEquals( Long.valueOf( 10 ), key.getId() );
        assertNull( key.getName() );

        Key parentKey = key.getParent();
        assertEquals( "Partner", parentKey.getKind() );
        assertEquals( Long.valueOf( 1 ), parentKey.getId() );
        assertNull( parentKey.getName() );
    }

    @Test
    public void convertStringToKey_AncestorName()
    {
        Key key = converter.convertFromRawKey( "Country:SK>Region:BA" );

        assertEquals( "Region", key.getKind() );
        assertNull( key.getId() );
        assertEquals( "BA", key.getName() );

        Key parentKey = key.getParent();
        assertEquals( "Country", parentKey.getKind() );
        assertNull( parentKey.getId() );
        assertEquals( "SK", parentKey.getName() );
    }

    @Test
    public void convertStringToKey_AncestorNameAndId()
    {
        Key key = converter.convertFromRawKey( "Partner:1>Address:10>Country:SK" );

        assertEquals( "Country", key.getKind() );
        assertEquals( "SK", key.getName() );
        assertNull( key.getId() );

        Key parentKey = key.getParent();
        assertEquals( "Address", parentKey.getKind() );
        assertEquals( Long.valueOf( 10 ), parentKey.getId() );
        assertNull( parentKey.getName() );

        Key parentParentKey = parentKey.getParent();
        assertEquals( "Partner", parentParentKey.getKind() );
        assertEquals( Long.valueOf( 1 ), parentParentKey.getId() );
        assertNull( parentParentKey.getName() );
    }

    // -- raw key to PathElements

    @Test
    public void convertStringToPathElements_SimpleId()
    {
        List<com.google.datastore.v1.Key.PathElement> pathElements = converter.convertToPathElements( "Partner:1" );

        assertEquals( 1, pathElements.size() );
        assertEquals( "Partner", pathElements.get( 0 ).getKind() );
        assertEquals( 1, pathElements.get( 0 ).getId() );
        assertTrue( pathElements.get( 0 ).getName().isEmpty() );
    }

    @Test
    public void convertStringToPathElements_SimpleName()
    {
        List<com.google.datastore.v1.Key.PathElement> pathElements = converter.convertToPathElements( "Country:SK" );

        assertEquals( 1, pathElements.size() );
        assertEquals( "Country", pathElements.get( 0 ).getKind() );
        assertEquals( 0, pathElements.get( 0 ).getId() );
        assertEquals( "SK", pathElements.get( 0 ).getName() );
    }

    @Test
    public void convertStringToPathElements_AncestorId()
    {
        List<com.google.datastore.v1.Key.PathElement> pathElements = converter.convertToPathElements( "Partner:1>Address:10" );

        assertEquals( 2, pathElements.size() );

        assertEquals( "Partner", pathElements.get( 0 ).getKind() );
        assertEquals( 1, pathElements.get( 0 ).getId() );
        assertTrue( pathElements.get( 0 ).getName().isEmpty() );

        assertEquals( "Address", pathElements.get( 1 ).getKind() );
        assertEquals( 10, pathElements.get( 1 ).getId() );
        assertTrue( pathElements.get( 1 ).getName().isEmpty() );
    }

    @Test
    public void convertStringToPathElements_AncestorName()
    {
        List<com.google.datastore.v1.Key.PathElement> pathElements = converter.convertToPathElements( "Country:SK>Region:BA" );

        assertEquals( 2, pathElements.size() );

        assertEquals( "Country", pathElements.get( 0 ).getKind() );
        assertEquals( 0, pathElements.get( 0 ).getId() );
        assertEquals( "SK", pathElements.get( 0 ).getName() );

        assertEquals( "Region", pathElements.get( 1 ).getKind() );
        assertEquals( 0, pathElements.get( 1 ).getId() );
        assertEquals( "BA", pathElements.get( 1 ).getName() );
    }

    @Test
    public void convertStringToPathElements_AncestorNameAndId()
    {
        List<com.google.datastore.v1.Key.PathElement> pathElements = converter.convertToPathElements( "Partner:1>Address:10>Country:SK" );

        assertEquals( 3, pathElements.size() );

        assertEquals( "Partner", pathElements.get( 0 ).getKind() );
        assertEquals( 1, pathElements.get( 0 ).getId() );
        assertTrue( pathElements.get( 0 ).getName().isEmpty() );

        assertEquals( "Address", pathElements.get( 1 ).getKind() );
        assertEquals( 10, pathElements.get( 1 ).getId() );
        assertTrue( pathElements.get( 1 ).getName().isEmpty() );

        assertEquals( "Country", pathElements.get( 2 ).getKind() );
        assertEquals( 0, pathElements.get( 2 ).getId() );
        assertEquals( "SK", pathElements.get( 2 ).getName() );
    }
}