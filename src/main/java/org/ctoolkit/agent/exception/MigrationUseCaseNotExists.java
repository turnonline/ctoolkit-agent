package org.ctoolkit.agent.exception;

/**
 * Exception thrown when no use case is found for migration
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrationUseCaseNotExists
        extends RuntimeException
{
    public MigrationUseCaseNotExists()
    {
    }

    public MigrationUseCaseNotExists( String message )
    {
        super( message );
    }
}
