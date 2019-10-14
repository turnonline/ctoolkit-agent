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

package biz.turnonline.ecosystem.service.converter;

import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.service.enricher.EnricherExecutor;
import biz.turnonline.ecosystem.service.transformer.TransformerExecutor;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static biz.turnonline.ecosystem.Mocks.mockImportSetProperty;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_BIN_DATA;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_BOOL;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_DATE;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_DOUBLE;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_LONG;
import static biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat.TYPE_STRING;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link ConverterExecutor} - mongo registrat - convert for import
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ConverterExecutorImportMongoTest
{
    private ConverterExecutor executor = new ConverterExecutor( new EnricherExecutor(), new TransformerExecutor(), new MongoConverterRegistrat() );

    @Test
    public void test_String()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_STRING, "Boston" );
        String convertedValue = ( String ) executor.convertProperty( property );

        assertEquals( "Boston", convertedValue );
    }

    @Test
    public void test_Long()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_LONG, "1" );
        Long convertedValue = ( Long ) executor.convertProperty( property );

        assertEquals( Long.valueOf( 1 ), convertedValue );
    }

    @Test
    public void test_Double()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_DOUBLE, "1.2" );
        Double convertedValue = ( Double ) executor.convertProperty( property );

        assertEquals( Double.valueOf( 1.2 ), convertedValue );
    }

    @Test
    public void test_Date()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        ImportSetProperty property = mockImportSetProperty( TYPE_DATE, Long.valueOf( calendar.getTimeInMillis() ).toString() );
        Date convertedValue = ( Date ) executor.convertProperty( property );

        assertEquals( calendar.getTime(), convertedValue );
    }

    @Test
    public void test_Boolean_False()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_BOOL, "false" );
        Boolean convertedValue = ( Boolean ) executor.convertProperty( property );

        assertFalse( convertedValue );
    }

    @Test
    public void test_Boolean_True()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_BOOL, "true" );
        Boolean convertedValue = ( Boolean ) executor.convertProperty( property );

        assertTrue( convertedValue );
    }

    @Test
    public void test_Binary()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_BIN_DATA, "Sm9obg==" );
        byte[] convertedValue = ( byte[] ) executor.convertProperty( property );

        assertArrayEquals( new byte[]{'J', 'o', 'h', 'n'}, convertedValue );
    }
}