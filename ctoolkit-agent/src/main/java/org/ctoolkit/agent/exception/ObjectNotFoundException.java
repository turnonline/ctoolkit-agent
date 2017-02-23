package org.ctoolkit.agent.exception;

/**
 * Thrown when object not found
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ObjectNotFoundException
        extends RuntimeException
{
    public ObjectNotFoundException()
    {
    }

    public ObjectNotFoundException( String message )
    {
        super( message );
    }

    public ObjectNotFoundException( String message, Throwable cause )
    {
        super( message, cause );
    }
}