package org.ctoolkit.bulkloader.utils;

/**
 * Interface for compressing data
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface Compressor
{

    /**
     * Method encodes a byte array into compressed byte array
     *
     * @param source array to compress
     * @return compressed byte array
     */
    public byte[] compress( final byte[] source );

    /**
     * Method inflates a compressed byte array
     *
     * @param source array to inflates
     * @return inflated byte array
     */
    public byte[] inflate( final byte[] source );
}
