package org.ctoolkit.bulkloader.common;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class EntityEncoderException
        extends Exception
{
    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    private String message;

    /**
     * @param code
     * @param message
     */
    public EntityEncoderException( String message )
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
}
