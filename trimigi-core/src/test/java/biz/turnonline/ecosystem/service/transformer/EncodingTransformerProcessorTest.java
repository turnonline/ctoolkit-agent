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

package biz.turnonline.ecosystem.service.transformer;

import biz.turnonline.ecosystem.model.api.MigrationSetPropertyEncodingTransformer;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link EncodingTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class EncodingTransformerProcessorTest
{
    private EncodingTransformerProcessor processor = new EncodingTransformerProcessor();

    // -- encode

    @Test
    public void transform_encode_base16()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base16" );

        assertEquals( "4A6F686E", processor.transform( "John", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_encode_base32()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base32" );

        assertEquals( "JJXWQ3Q=", processor.transform( "John", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_encode_base32Hex()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base32Hex" );

        assertEquals( "99NMGRG=", processor.transform( "John", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_encode_base64()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64" );

        assertEquals( "Sm9obg==", processor.transform( "John", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_encode_base64Url()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64Url" );

        assertEquals( "Sm9obg==", processor.transform( "John", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_encode_base64_byteArray()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64" );

        assertEquals( "Sm9obg==", processor.transform( new byte[]{'J','o','h','n'}, transformer, new HashMap<>() ) );
    }

    // -- decode

    @Test
    public void transform_decode_base16()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base16" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( "4A6F686E", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_decode_base32()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base32" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( "JJXWQ3Q=", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_decode_base32Hex()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base32Hex" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( "99NMGRG=", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_decode_base64()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( "Sm9obg==", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_decode_base64Url()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64Url" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( "Sm9obg==", transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_decode_base64_byteArray()
    {
        MigrationSetPropertyEncodingTransformer transformer = new MigrationSetPropertyEncodingTransformer();
        transformer.setEncodingType( "base64" );
        transformer.setOperation( "decode" );

        assertEquals( "John", processor.transform( new byte[]{'S','m','9','o','b','g','=','='}, transformer, new HashMap<>() ) );
    }
}