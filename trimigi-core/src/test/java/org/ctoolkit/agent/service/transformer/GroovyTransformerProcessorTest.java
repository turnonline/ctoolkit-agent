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

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.ctoolkit.agent.model.api.MigrationSetPropertyGroovyTransformer;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class GroovyTransformerProcessorTest
{
    private GroovyTransformerProcessor processor = new GroovyTransformerProcessor();

    @Test
    public void transform() throws Exception
    {
        InputStream groovyStream = GroovyTransformerProcessorTest.class.getResourceAsStream( "/transformer.groovy" );
        String command = CharStreams.toString( new InputStreamReader( groovyStream, Charsets.UTF_8 ) );

        MigrationSetPropertyGroovyTransformer transformer = new MigrationSetPropertyGroovyTransformer();
        transformer.setCommand( command );

        Map<String, Object> ctx = new HashMap<>();
        ctx.put( "rootNumber", 12345 );
        ctx.put( "sequenceNumber", 1 );

        Object value = processor.transform( "Foo", transformer, ctx );

        assertEquals( "Foo12346", value );
        assertEquals( 12346, ctx.get( "fullNumber" ) );
    }
}