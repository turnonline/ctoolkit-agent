package org.ctoolkit.agent.common;

/**
 * The agent specific backend exception.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentException
        extends Exception
{
    private static final long serialVersionUID = 1L;

    public AgentException()
    {
    }

    public AgentException( String message )
    {
        super( message );
    }

    public AgentException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public AgentException( Throwable cause )
    {
        super( cause );
    }

    public AgentException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
