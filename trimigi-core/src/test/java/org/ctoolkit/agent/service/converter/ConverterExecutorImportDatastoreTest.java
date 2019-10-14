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

package org.ctoolkit.agent.service.converter;

import org.ctoolkit.agent.model.LatLng;
import org.ctoolkit.agent.model.RawKey;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.service.enricher.EnricherExecutor;
import org.ctoolkit.agent.service.transformer.TransformerExecutor;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.ctoolkit.agent.Mocks.mockImportSetProperty;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_BINARY;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_BOOLEAN;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_DATE;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_DOUBLE;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_KEY;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_LAT_LNG;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_LONG;
import static org.ctoolkit.agent.service.converter.DatastoreConverterRegistrat.TYPE_STRING;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link ConverterExecutor} - datastore registrat - convert for import
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ConverterExecutorImportDatastoreTest
{
    private ConverterExecutor executor = new ConverterExecutor( new EnricherExecutor(), new TransformerExecutor(), new DatastoreConverterRegistrat() );

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
        ImportSetProperty property = mockImportSetProperty( TYPE_BOOLEAN, "false" );
        Boolean convertedValue = ( Boolean ) executor.convertProperty( property );

        assertFalse( convertedValue );
    }

    @Test
    public void test_Boolean_True()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_BOOLEAN, "true" );
        Boolean convertedValue = ( Boolean ) executor.convertProperty( property );

        assertTrue( convertedValue );
    }

    @Test
    public void test_Binary()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_BINARY, "Sm9obg==" );
        byte[] convertedValue = ( byte[] ) executor.convertProperty( property );

        assertArrayEquals( new byte[]{'J', 'o', 'h', 'n'}, convertedValue );
    }

    @Test
    public void test_LatLnt()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_LAT_LNG, "13.4:14.5" );
        LatLng convertedValue = ( LatLng ) executor.convertProperty( property );

        assertEquals( 13.4, convertedValue.getLatitude(), 0 );
        assertEquals( 14.5, convertedValue.getLongitude(), 0 );
    }

    @Test
    public void test_Key()
    {
        ImportSetProperty property = mockImportSetProperty( TYPE_KEY, "Partner:1" );
        RawKey convertedValue = ( RawKey ) executor.convertProperty( property );

        assertEquals( "Partner:1", convertedValue.getRawKey() );
    }
}