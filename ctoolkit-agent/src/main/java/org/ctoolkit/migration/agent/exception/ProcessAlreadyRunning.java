package org.ctoolkit.migration.agent.exception;

/**
 * Exception thrown when trying to start job but previous job is still running
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ProcessAlreadyRunning
        extends RuntimeException
{
    public ProcessAlreadyRunning( String message )
    {
        super( message );
    }
}
