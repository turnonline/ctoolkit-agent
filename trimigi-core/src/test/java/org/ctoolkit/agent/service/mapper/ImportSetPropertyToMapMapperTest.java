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

package org.ctoolkit.agent.service.mapper;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.converter.MongoConverterRegistrat;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportSetPropertyToMapMapperTest
{
    private ConverterExecutor converterExecutor = new ConverterExecutor( new MongoConverterRegistrat() );
    private ImportSetPropertyToMapMapper mapper = new ImportSetPropertyToMapMapper(converterExecutor);

    @Test
    public void map_Simple()
    {
        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "name" );
        importSetProperty.setType( "string" );
        importSetProperty.setValue( "John" );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "John", root.get( "name" ) );
    }

    @Test
    public void map_Embedded()
    {
        ImportSetProperty city = new ImportSetProperty();
        city.setName( "city" );
        city.setType( "string" );
        city.setValue( "New York" );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "object" );
        importSetProperty.setValue( Collections.singletonList( city ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "New York", ( ( Map ) root.get( "address" ) ).get( "city" ) );
    }

    @Test
    public void map_List()
    {
        ImportSetProperty code1 = new ImportSetProperty();
        code1.setType( "string" );
        code1.setValue( "XFD" );

        ImportSetProperty code2 = new ImportSetProperty();
        code2.setType( "string" );
        code2.setValue( "LLK" );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "list" );
        importSetProperty.setValue( Arrays.asList( code1, code2 ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "XFD", ( ( List ) root.get( "address" ) ).get( 0 ) );
        assertEquals( "LLK", ( ( List ) root.get( "address" ) ).get( 1 ) );
    }

    @Test
    public void map_EmbeddedEmbedded()
    {
        ImportSetProperty cityPart = new ImportSetProperty();
        cityPart.setName( "cityPart" );
        cityPart.setType( "string" );
        cityPart.setValue( "New York - west" );

        ImportSetProperty city = new ImportSetProperty();
        city.setName( "city" );
        city.setType( "object" );
        city.setValue( Collections.singletonList( cityPart ) );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "object" );
        importSetProperty.setValue( Collections.singletonList( city ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "New York - west", ( ( Map ) ( ( Map ) root.get( "address" ) ).get( "city" ) ).get( "cityPart" ) );
    }

    @Test
    public void map_EmbeddedList()
    {
        ImportSetProperty code1 = new ImportSetProperty();
        code1.setType( "string" );
        code1.setValue( "XFD" );

        ImportSetProperty code2 = new ImportSetProperty();
        code2.setType( "string" );
        code2.setValue( "LLK" );

        ImportSetProperty city = new ImportSetProperty();
        city.setName( "city" );
        city.setType( "list" );
        city.setValue( Arrays.asList( code1, code2 ) );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "object" );
        importSetProperty.setValue( Collections.singletonList( city ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "XFD", ( ( List ) ( ( Map ) root.get( "address" ) ).get( "city" ) ).get( 0 ) );
        assertEquals( "LLK", ( ( List ) ( ( Map ) root.get( "address" ) ).get( "city" ) ).get( 1 ) );
    }

    @Test
    public void map_ListList()
    {
        ImportSetProperty code1 = new ImportSetProperty();
        code1.setType( "string" );
        code1.setValue( "XFD" );

        ImportSetProperty code2 = new ImportSetProperty();
        code2.setType( "list" );
        code2.setValue( Collections.singletonList( code1 ) );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "list" );
        importSetProperty.setValue( Collections.singletonList( code2 ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "XFD", ( ( List ) ( ( List ) root.get( "address" ) ).get( 0 ) ).get( 0 ) );
    }

    @Test
    public void map_ListEmbedded()
    {
        ImportSetProperty cityPart = new ImportSetProperty();
        cityPart.setName( "cityPart" );
        cityPart.setType( "string" );
        cityPart.setValue( "New York - west" );

        ImportSetProperty city = new ImportSetProperty();
        city.setType( "object" );
        city.setValue( Collections.singletonList( cityPart ) );

        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( "address" );
        importSetProperty.setType( "list" );
        importSetProperty.setValue( Collections.singletonList( city ) );

        Map<String, Object> root = new HashMap<>();

        mapper.map( importSetProperty, root );

        assertEquals( 1, root.size() );
        assertEquals( "New York - west", ( ( Map ) ( ( List ) root.get( "address" ) ).get( 0 ) ).get( "cityPart" ) );
    }
}