package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.transformer.TransformerExecutor;
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
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_BIN_DATA;
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_BOOL;
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_DATE;
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_DOUBLE;
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_LONG;
import static org.ctoolkit.agent.converter.MongoConverterRegistrat.TYPE_STRING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link ConverterExecutor} - elasticsearch registrat - convert for migration
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MongoConverterMigrationExecutorTest
{
    private ConverterExecutor executor = new ConverterExecutor(new TransformerExecutor(), new MongoConverterRegistrat());
    
    // -- target string

    @Test
    public void test_String_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( "Boston", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "Boston", importSetProperty.getValue() );
    }

    @Test
    public void test_Integer_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( 1, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Long_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( 1L, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Float_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( 1F, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_Double_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( 1D, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "1.0", importSetProperty.getValue() );
    }

    @Test
    public void test_BigDecimal_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( BigDecimal.valueOf( 1 ), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "1", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_String()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        ImportSetProperty importSetProperty = executor.convertProperty( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_String()
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( new byte[]{'J', 'o', 'h', 'n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_String() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_String() throws SQLException
    {
        MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();

        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
        assertEquals( "John", importSetProperty.getValue() );
    }

    @Test
    public void test_Date_String()
    {
        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 2, 0, 0, 0 );

        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "yyyy-MM-dd" );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_STRING );
        property.getTransformers().add( transformer );
        ImportSetProperty importSetProperty = executor.convertProperty( calendar.getTime(), property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "string", importSetProperty.getType() );
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
    public void test_String_Bool()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BOOL );
        ImportSetProperty importSetProperty = executor.convertProperty( "true", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "bool", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    @Test
    public void test_Boolean_Bool()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BOOL );
        ImportSetProperty importSetProperty = executor.convertProperty( true, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "bool", importSetProperty.getType() );
        assertEquals( "true", importSetProperty.getValue() );
    }

    // -- type binary

    @Test
    public void test_String_BinData()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BIN_DATA );
        ImportSetProperty importSetProperty = executor.convertProperty( "John", property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binData", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_ByteArray_BinData()
    {
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BIN_DATA );
        ImportSetProperty importSetProperty = executor.convertProperty( new byte[]{'J', 'o', 'h', 'n'}, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binData", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Clob_BinData() throws SQLException
    {
        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );
        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BIN_DATA );
        ImportSetProperty importSetProperty = executor.convertProperty( clob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binData", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }

    @Test
    public void test_Blob_BinData() throws SQLException
    {
        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        MigrationSetProperty property = mockMigrationSetProperty( TYPE_BIN_DATA );
        ImportSetProperty importSetProperty = executor.convertProperty( blob, property );

        assertEquals( "name", importSetProperty.getName() );
        assertEquals( "binData", importSetProperty.getType() );
        assertEquals( "Sm9obg==", importSetProperty.getValue() );
    }
}