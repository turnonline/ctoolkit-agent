package org.ctoolkit.agent.converter.elasticsearch;

import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.transformer.DateTransformerProcessor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;

import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_BINARY;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_BOOLEAN;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_DATE;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_DOUBLE;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_KEYWORD;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_LONG;
import static org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat.TYPE_TEXT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link ElasticsearchConverterRegistrat}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ElasticsearchConverterRegistratTest
{
    private ElasticsearchConverterRegistrat registrat = new ElasticsearchConverterRegistrat();

    // -- target text

    @Test
    public void test_String_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( "Boston", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "Boston", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Text()
    {
        MigrationSetProperty property = mockText( TYPE_TEXT );
        ImportSetProperty importSetProperty = registrat.convert( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Text()
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        MigrationSetProperty property = mockText( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( new byte[]{'J', 'o', 'h', 'n'}, property );

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
        MigrationSetProperty property = mockText( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( clob, property );

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

        MigrationSetProperty property = mockText( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Text()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockText( TYPE_TEXT );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "text", importSetProperty.getType() );
        assertEquals( "2018-01-02", importSetProperty.getValue() );
    }

    // -- target keyword

    @Test
    public void test_String_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( "Boston", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "Boston", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Keyword()
    {
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        ImportSetProperty importSetProperty = registrat.convert( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Keyword()
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( new byte[]{'J', 'o', 'h', 'n'}, property );

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
        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( clob, property );

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

        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Keyword()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockText( TYPE_KEYWORD );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "keyword", importSetProperty.getType() );
        assertEquals( "2018-01-02", importSetProperty.getValue() );
    }

    // -- type long

    @Test
    public void test_String_Long()
    {
        MigrationSetProperty property = mockText( TYPE_LONG );
        ImportSetProperty importSetProperty = registrat.convert( "1", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Long()
    {
        MigrationSetProperty property = mockText( TYPE_LONG );
        ImportSetProperty importSetProperty = registrat.convert( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Long()
    {
        MigrationSetProperty property = mockText( TYPE_LONG );
        ImportSetProperty importSetProperty = registrat.convert( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Long()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( DateTransformerProcessor.CONST_EPOCH );

        MigrationSetProperty property = mockText( TYPE_LONG );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "long", importSetProperty.getType() );
        assertEquals( "1514847600000", importSetProperty.getValue() );
    }

    // -- type double

    @Test
    public void test_String_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( "1", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_Double()
    {
        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        ImportSetProperty importSetProperty = registrat.convert( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Double()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( DateTransformerProcessor.CONST_EPOCH );

        MigrationSetProperty property = mockText( TYPE_DOUBLE );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "double", importSetProperty.getType() );
        assertEquals( "1.5148476E12", importSetProperty.getValue() );
    }

    // -- type date

    @Test
    public void test_String_Date()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockText( TYPE_DATE );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = registrat.convert( "2018-01-01", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514761200000", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_Date()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetProperty property = mockText( TYPE_DATE );
        ImportSetProperty importSetProperty = registrat.convert( 1514847600000L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514847600000", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_Date()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        MigrationSetProperty property = mockText( TYPE_DATE );
        ImportSetProperty importSetProperty = registrat.convert( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "date", importSetProperty.getType() );
        assertEquals( "1514847600000", importSetProperty.getValue() );
    }

    // -- type boolean

    @Test
    public void test_String_Boolean()
    {
        MigrationSetProperty property = mockText( TYPE_BOOLEAN );
        ImportSetProperty importSetProperty = registrat.convert( "true", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "boolean", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Boolean()
    {
        MigrationSetProperty property = mockText( TYPE_BOOLEAN );
        ImportSetProperty importSetProperty = registrat.convert( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "boolean", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    // -- type binary

    @Test
    public void test_String_Binary()
    {
        MigrationSetProperty property = mockText( TYPE_BINARY );
        ImportSetProperty importSetProperty = registrat.convert( "John", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_Binary()
    {
        MigrationSetProperty property = mockText( TYPE_BINARY );
        ImportSetProperty importSetProperty = registrat.convert( new byte[]{'J','o','h','n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_Binary() throws SQLException
    {
        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockText( TYPE_BINARY );
        ImportSetProperty importSetProperty = registrat.convert( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_Binary() throws SQLException
    {
        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockText( TYPE_BINARY );
        ImportSetProperty importSetProperty = registrat.convert( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binary", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    // -- mocks

    private MigrationSetProperty mockText( String type )
    {
        MigrationSetProperty property = new MigrationSetProperty();
        property.setTargetType( type );
        property.setTargetProperty( "name" );
        return property;
    }
}