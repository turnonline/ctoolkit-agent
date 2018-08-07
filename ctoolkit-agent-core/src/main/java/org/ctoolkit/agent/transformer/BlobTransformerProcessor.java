package org.ctoolkit.agent.transformer;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Transformer transforms blob values (byte array, clob, blob) into string
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BlobTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyBlobTransformer>
{
    private static Logger log = LoggerFactory.getLogger( BlobTransformerProcessor.class );

    @Override
    public Object transform( Object value, MigrationSetPropertyBlobTransformer transformer )
    {
        if ( value instanceof byte[] )
        {
            value = new String( ( byte[] ) value, Charsets.UTF_8 );
        }
        else if ( value instanceof Blob )
        {
            try
            {
                InputStream stream = ( ( Blob ) value ).getBinaryStream();
                byte[] data = new byte[stream.available()];
                ByteStreams.readFully( stream, data );

                value = new String( data, Charsets.UTF_8 );
            }
            catch ( SQLException | IOException e )
            {
                log.info( "Unable to read Blob data", e );
            }
        }
        else if ( value instanceof Clob )
        {
            try
            {
                Reader reader = ( ( Clob ) value ).getCharacterStream();
                value = CharStreams.toString( reader );
            }
            catch ( SQLException | IOException e )
            {
                log.info( "Unable to read Clob data", e );
            }
        }

        if ( transformer.getEncodeToBase64() && value instanceof String )
        {
            value = BaseEncoding.base64().encode( ( ( String ) value ).getBytes( Charsets.UTF_8 ) );
        }

        return value;
    }
}
