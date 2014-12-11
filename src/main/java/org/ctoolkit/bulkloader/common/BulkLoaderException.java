/**
 *
 */
package org.ctoolkit.bulkloader.common;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderException
        extends Exception
{

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * exception reason
     */
    private Long reason;

    /**
     * @param message
     */
    public BulkLoaderException( String message )
    {
        super( message );
    }

    /**
     * @param message
     */
    public BulkLoaderException( Long reason, String message )
    {
        super( message );
        this.reason = reason;
    }

    /**
     * @param message
     */
    public BulkLoaderException( String message, Exception cause )
    {
        super( message, cause );
    }

    /**
     * @param message
     */
    public BulkLoaderException( Long reason, String message, Exception cause )
    {
        super( message, cause );
        this.reason = reason;
    }

    /**
     * @return the reason
     */
    public Long getReason()
    {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason( Long reason )
    {
        this.reason = reason;
    }
}
