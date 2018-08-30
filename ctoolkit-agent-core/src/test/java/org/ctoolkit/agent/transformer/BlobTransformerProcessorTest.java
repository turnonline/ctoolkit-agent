package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link BlobTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BlobTransformerProcessorTest
{
    private BlobTransformerProcessor processor = new BlobTransformerProcessor();

    @Test
    public void transform_ByteArrayToString()
    {
        byte[] data = new byte[]{'J', 'o', 'h', 'n'};
        assertEquals( "John", processor.transform( data, new MigrationSetPropertyBlobTransformer(), new HashMap<>() ) );
    }

    @Test
    public void transform_BlobToString() throws Exception
    {
        Blob blob = mock( Blob.class );
        when( blob.getBinaryStream() ).thenReturn( new ByteArrayInputStream( new byte[]{'J', 'o', 'h', 'n'} ) );

        assertEquals( "John", processor.transform( blob, new MigrationSetPropertyBlobTransformer(), new HashMap<>() ) );
    }

    @Test
    public void transform_ClobToString() throws Exception
    {
        Clob clob = mock( Clob.class );
        when( clob.getCharacterStream() ).thenReturn( new StringReader( "John" ) );

        assertEquals( "John", processor.transform( clob, new MigrationSetPropertyBlobTransformer(), new HashMap<>() ) );
    }
}