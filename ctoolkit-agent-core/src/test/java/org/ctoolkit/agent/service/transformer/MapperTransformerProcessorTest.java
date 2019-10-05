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

package org.ctoolkit.agent.service.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformerMappings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link MapperTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MapperTransformerProcessorTest
{
    private MapperTransformerProcessor processor = new MapperTransformerProcessor();

    @Test
    public void transformMale()
    {
        assertEquals( "MALE", processor.transform( "M", mockTransformer(), new HashMap<>() ) );
    }

    @Test
    public void transformFemale()
    {
        assertEquals( "FEMALE", processor.transform( "F", mockTransformer(), new HashMap<>() ) );
    }

    @Test
    public void transformUnknown()
    {
        assertEquals( "X", processor.transform( "X", mockTransformer(), new HashMap<>() ) );
    }

    private MigrationSetPropertyMapperTransformer mockTransformer()
    {
        MigrationSetPropertyMapperTransformerMappings mappings1 = new MigrationSetPropertyMapperTransformerMappings();
        mappings1.setSource( "M" );
        mappings1.setTarget( "MALE" );

        MigrationSetPropertyMapperTransformerMappings mappings2 = new MigrationSetPropertyMapperTransformerMappings();
        mappings2.setSource( "F" );
        mappings2.setTarget( "FEMALE" );

        MigrationSetPropertyMapperTransformer transformer = new MigrationSetPropertyMapperTransformer();
        transformer.setMappings( new ArrayList<>() );
        transformer.getMappings().add( mappings1 );
        transformer.getMappings().add( mappings2 );

        return transformer;
    }
}