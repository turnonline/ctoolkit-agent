package org.ctoolkit.agent.service.impl.datastore;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.LatLng;
import com.google.cloud.datastore.LatLngValue;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Unit test for {@link EntityEncoder}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityEncoderTest
{
    private EntityEncoder encoder = new EntityEncoder();

    // -- test encode

    @Test( expected = IllegalArgumentException.class )
    public void testEncode_UnknownType()
    {
        try
        {
            encoder.encode( "prop", new LatLngValue( LatLng.of( 1, 1 ) ) );
            fail( IllegalArgumentException.class.getName() + " expected!" );
        }
        catch ( IllegalArgumentException e )
        {
            assertEquals( "Unknown entity type 'com.google.cloud.datastore.LatLngValue'", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testEncode_Null()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", null );

        assertEquals( "prop", prop.getName() );
        assertEquals( "null", prop.getType() );
        assertEquals( null, prop.getValue() );
    }

    @Test
    public void testEncode_NullValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new NullValue() );

        assertEquals( "prop", prop.getName() );
        assertEquals( "null", prop.getType() );
        assertEquals( null, prop.getValue() );
    }

    @Test
    public void testEncode_StringValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new StringValue( "John" ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "string", prop.getType() );
        assertEquals( "John", prop.getValue() );
    }

    @Test
    public void testEncode_DoubleValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new DoubleValue( 1D ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "double", prop.getType() );
        assertEquals( "1.0", prop.getValue() );
    }

    @Test
    public void testEncode_LongValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new LongValue( 1L ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "long", prop.getType() );
        assertEquals( "1", prop.getValue() );
    }

    @Test
    public void testEncode_BooleanValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new BooleanValue( true ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "boolean", prop.getType() );
        assertEquals( "true", prop.getValue() );
    }

    @Test
    public void testEncode_DateValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "date", prop.getType() );
        assertEquals( "1499287122907", prop.getValue() );
    }

    @Test
    public void testEncode_BlobValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new BlobValue( Blob.copyFrom( "1".getBytes() ) ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "blob", prop.getType() );
        assertEquals( "MQ==", prop.getValue() );
    }

    @Test
    public void testEncode_KeyValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1L ).build() ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "reference", prop.getType() );
        assertEquals( "Person:1", prop.getValue() );
    }

    @Test
    public void testEncode_ListLongValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new ListValue( new LongValue( 1 ), new LongValue( 2 ) ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "long", prop.getType() );
        assertEquals( "1,2", prop.getValue() );
    }

    @Test
    public void testEncode_ListStringValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new ListValue( new StringValue( "John" ), new StringValue( "Foo" ) ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "string", prop.getType() );
        assertEquals( "John,Foo", prop.getValue() );
    }

    @Test
    public void testEncode_ListKeyValue()
    {
        ChangeSetEntityProperty prop = encoder.encode( "prop", new ListValue(
                new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1L ).build() ),
                new KeyValue( Key.newBuilder( "c-toolkit", "Person", 2L ).build() )
        ) );

        assertEquals( "prop", prop.getName() );
        assertEquals( "reference", prop.getType() );
        assertEquals( "Person:1,Person:2", prop.getValue() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testEncode_ListValue_Unknown()
    {
        try
        {
            encoder.encode( "prop", new ListValue( new DoubleValue( 1D ), new DoubleValue( 2D ) ) );
            fail( IllegalArgumentException.class.getName() + " expected!" );
        }
        catch ( IllegalArgumentException e )
        {
            assertEquals( "Unknown list type: com.google.cloud.datastore.DoubleValue", e.getMessage() );
            throw e;
        }
    }

    // -- test formatKey

    @Test
    public void testFormatKey_Null()
    {
        assertNull( encoder.formatKey( null ) );
    }

    @Test
    public void testFormatKey_OneLevel()
    {
        Key key = Key.newBuilder( "c-toolkit", "Person", 1L ).build();
        assertEquals( "Person:1", encoder.formatKey( key ) );
    }

    @Test
    public void testFormatKey_TwoLevels()
    {
        Key parentKey = Key.newBuilder( "c-toolkit", "Person", 1L ).build();
        Key key = Key.newBuilder( parentKey, "Address", 10L ).build();

        assertEquals( "Person:1::Address:10", encoder.formatKey( key ) );
    }
}