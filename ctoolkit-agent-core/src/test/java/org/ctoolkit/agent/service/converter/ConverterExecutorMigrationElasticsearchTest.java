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
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.service.enricher.EnricherExecutor;
import org.ctoolkit.agent.service.transformer.TransformerExecutor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import static org.ctoolkit.agent.Mocks.mockMigrationSetProperty;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_BINARY;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_BOOLEAN;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_DATE;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_DOUBLE;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_GEO_POINT;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_KEYWORD;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_LONG;
import static org.ctoolkit.agent.service.converter.ElasticsearchConverterRegistrat.TYPE_TEXT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link ConverterExecutor} - elasticsearch registrat - convert for migration
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ConverterExecutorMigrationElasticsearchTest
{
    private ConverterExecutor executor = new ConverterExecutor(new EnricherExecutor(), new TransformerExecutor(), new ElasticsearchConverterRegistrat());
    
    // -- target text

    @Test
    public void test_String_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( "Boston", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "Boston", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Text()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        ImportSetProperty importSetProperty = executor.convertProperty( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Text()
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( new byte[]{'J', 'o', 'h', 'n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_Text() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_Text() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Text()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "2018-01-02", importSetProperty.getValue() );
    }

    // -- target keyword

    @Test
    public void test_String_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( "Boston", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "Boston", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Keyword()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = executor.convertProperty( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Keyword()
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( new byte[]{'J', 'o', 'h', 'n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_Keyword() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_Keyword() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Keyword()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "2018-01-02", importSetProperty.getValue() );
    }

    // -- type long

    @Test
    public void test_String_Long()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_LONG );
        ImportSetProperty importSetProperty = executor.convertProperty( "1", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Long()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_LONG );
        ImportSetProperty importSetProperty = executor.convertProperty( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Long()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_LONG );
        ImportSetProperty importSetProperty = executor.convertProperty( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Long()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setEpoch( true );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_LONG );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1514851200000", importSetProperty.getValue() );
    }

    // -- type double

    @Test
    public void test_String_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( "1", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Double()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = executor.convertProperty( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Double()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setEpoch( true );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DOUBLE );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.5148512E12", importSetProperty.getValue() );
    }

    // -- type date

    @Test
    public void test_String_Date()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DATE );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( "2018-01-01", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514764800000", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Date()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DATE );
        ImportSetProperty importSetProperty = executor.convertProperty( 1514847600000L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514847600000", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Date()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_DATE );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514851200000", importSetProperty.getValue() );
    }

    // -- type boolean

    @Test
    public void test_String_Boolean()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BOOLEAN );
        ImportSetProperty importSetProperty = executor.convertProperty( "true", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "boolean", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Boolean()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BOOLEAN );
        ImportSetProperty importSetProperty = executor.convertProperty( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "boolean", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    // -- type binary

    @Test
    public void test_String_Binary()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BINARY );
        ImportSetProperty importSetProperty = executor.convertProperty( "John", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Binary()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BINARY );
        ImportSetProperty importSetProperty = executor.convertProperty( new byte[]{'J', 'o', 'h', 'n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_Binary() throws SQLException
    {
        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BINARY );
        ImportSetProperty importSetProperty = executor.convertProperty( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_Binary() throws SQLException
    {
        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BINARY );
        ImportSetProperty importSetProperty = executor.convertProperty( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    // -- type geo point

    @Test
    public void test_GeoPoint()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_GEO_POINT );
        ImportSetProperty importSetProperty = executor.convertProperty( new LatLng( 12.3, 14.5 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "geo-point", importSetProperty.getType() );
        assertEquals( "12.3:14.5", importSetProperty.getValue() );
    }
}