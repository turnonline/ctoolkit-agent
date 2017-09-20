package org.ctoolkit.agent.service.impl.datastore;

import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link EntityDecoder}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityDecoderTest
{
    private EntityDecoder decoder = new EntityDecoder();

    @Before
    public void setUp() throws Exception
    {
        Deencapsulation.setField( decoder, "projectId", "c-toolkit" );
    }

    // -- test decode

    @Test
    public void testDecode_UnsupportedValue()
    {
        Value<?> value = decoder.decode( "bigDecimal", "1" );

        assertTrue( value instanceof NullValue );
    }

    @Test
    public void testDecode_NullValue()
    {
        Value<?> value = decoder.decode( null, null );

        assertTrue( value instanceof NullValue );
    }

    @Test
    public void testDecode_StringValue()
    {
        Value<?> value = decoder.decode( "string", "foo" );

        assertTrue( value instanceof StringValue );
        assertEquals( "foo", value.get() );
    }

    @Test
    public void testDecode_DoubleValue()
    {
        Value<?> value = decoder.decode( "double", "1" );

        assertTrue( value instanceof DoubleValue );
        assertEquals( 1D, value.get() );
    }

    @Test
    public void testDecode_LongValue()
    {
        Value<?> value = decoder.decode( "long", "1" );

        assertTrue( value instanceof LongValue );
        assertEquals( 1L, value.get() );
    }

    @Test
    public void testDecode_BooleanValue()
    {
        Value<?> value = decoder.decode( "boolean", "true" );

        assertTrue( value instanceof BooleanValue );
        assertEquals( true, value.get() );
    }

    @Test
    public void testDecode_DateValue()
    {
        Value<?> value = decoder.decode( "date", "1499287122907" );

        assertTrue( value instanceof TimestampValue );
        assertEquals( 1499287122907L, ( ( TimestampValue ) value ).get().toSqlTimestamp().getTime() );
    }

    @Test
    public void testDecode_BlobValue()
    {
        Value<?> value = decoder.decode( "blob", "MQ==" ); // base64 encoded '1'

        assertTrue( value instanceof BlobValue );
        assertEquals( "1", new String( ( ( BlobValue ) value ).get().toByteArray() ) );
    }

    @Test
    public void testDecode_KeyValue()
    {
        Value<?> value = decoder.decode( "reference", "Person:1" );

        assertTrue( value instanceof KeyValue );
        assertEquals( "Person", ( ( KeyValue ) value ).get().getKind() );
        assertEquals( Long.valueOf( 1 ), ( ( KeyValue ) value ).get().getId() );
    }

    @Test
    public void testDecode_ListLongValue()
    {
        Value<?> value = decoder.decode( "long", "1,2" );

        assertTrue( value instanceof ListValue );
        assertEquals( 1L, ( ( ListValue ) value ).get().get( 0 ).get() );
        assertEquals( 2L, ( ( ListValue ) value ).get().get( 1 ).get() );
    }

    @Test
    public void testDecode_ListStringValue()
    {
        Value<?> value = decoder.decode( "string", "John,Foo" );

        assertTrue( value instanceof ListValue );
        assertEquals( "John", ( ( ListValue ) value ).get().get( 0 ).get() );
        assertEquals( "Foo", ( ( ListValue ) value ).get().get( 1 ).get() );
    }

    @Test
    public void testDecode_ListKeyValue()
    {
        Value<?> value = decoder.decode( "reference", "Person:1,Person:2" );

        assertTrue( value instanceof ListValue );

        KeyValue keyValue1 = ( KeyValue ) ( ( ListValue ) value ).get().get( 0 );
        KeyValue keyValue2 = ( KeyValue ) ( ( ListValue ) value ).get().get( 1 );

        assertEquals( "Person", keyValue1.get().getKind() );
        assertEquals( Long.valueOf( 1 ), keyValue1.get().getId() );
        assertEquals( "Person", keyValue2.get().getKind() );
        assertEquals( Long.valueOf( 2 ), keyValue2.get().getId() );
    }

    // -- test parseKeyByIdOrName

    @Test
    public void testParseKeyByIdOrName_OneLevel_ById()
    {
        Key key = decoder.parseKeyByIdOrName( "Person:1" );

        assertEquals( "Person", key.getKind() );
        assertEquals( Long.valueOf( 1 ), key.getId() );
        assertNull( key.getName() );
        assertNull( key.getParent() );
    }

    @Test
    public void testParseKeyByIdOrName_TwoLevels_ById()
    {
        Key key = decoder.parseKeyByIdOrName( "Person:1::Address:10" );

        assertEquals( "Address", key.getKind() );
        assertEquals( Long.valueOf( 10 ), key.getId() );
        assertNull( key.getName() );

        assertEquals( "Person", key.getParent().getKind() );
        assertEquals( Long.valueOf( 1 ), key.getParent().getId() );
        assertNull( key.getParent().getName() );
    }

    @Test
    public void testParseKeyByIdOrName_OneLevel_ByName()
    {
        Key key = decoder.parseKeyByIdOrName( "Person:FOO" );

        assertEquals( "Person", key.getKind() );
        assertNull( key.getId() );
        assertEquals( "FOO", key.getName() );
        assertNull( key.getParent() );
    }

    @Test
    public void testParseKeyByIdOrName_TwoLevels_ByName()
    {
        Key key = decoder.parseKeyByIdOrName( "Person:FOO::Address:BAR" );

        assertEquals( "Address", key.getKind() );
        assertNull( key.getId() );
        assertEquals( "BAR", key.getName() );

        assertEquals( "Person", key.getParent().getKind() );
        assertNull( key.getParent().getId() );
        assertEquals( "FOO", key.getParent().getName() );
    }
}