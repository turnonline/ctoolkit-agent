package org.ctoolkit.bulkloader.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * ZIP compress/inflate
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ZIPCompressor
        implements Compressor
{

    /**
     * Logger for this class
     */
    private final static Logger logger = Logger.getLogger( ZIPCompressor.class.getName() );

    /**
     * Default compressor
     */
    public ZIPCompressor()
    {
    }

    /**
     * Method encodes a byte array into compressed byte array
     *
     * @param source array to compress
     * @return compressed byte array
     */
    @Override
    public byte[] compress( byte[] source )
    {
        Deflater compressor = new Deflater();
        compressor.setLevel( Deflater.BEST_COMPRESSION );
        compressor.setInput( source );
        compressor.finish();

        ByteArrayOutputStream bos = new ByteArrayOutputStream( source.length );

        // Compress the data
        byte[] buf = new byte[4096];
        while ( !compressor.finished() )
        {
            int count = compressor.deflate( buf );
            bos.write( buf, 0, count );
        }
        try
        {
            bos.close();
        }
        catch ( IOException e )
        {
            logger.severe( e.getMessage() );
            return null;
        }

        // Get the compressed data
        return bos.toByteArray();
    }

    @Override
    /**
     * Method inflates a compressed byte array
     *
     * @param source
     *            array to inflates
     * @return inflated byte array
     * @throws CompressorException
     *             if something went wrong
     */
    public byte[] inflate( byte[] source )
    {
        Inflater inflator = new Inflater();
        inflator.setInput( source );
        ByteArrayOutputStream bos = new ByteArrayOutputStream( source.length );
        byte[] buf = new byte[4096];
        try
        {
            while ( true )
            {
                int count = inflator.inflate( buf );
                if ( count > 0 )
                {
                    bos.write( buf, 0, count );
                }
                else if ( count == 0 && inflator.finished() )
                {
                    break;
                }
                else
                {
                    logger.severe( "bad zip data, size:" + source.length );
                    return null;
                }
            }
        }
        catch ( DataFormatException t )
        {
            logger.severe( t.getMessage() );
            return null;
        }
        finally
        {
            inflator.end();
        }
        return bos.toByteArray();
    }
}
