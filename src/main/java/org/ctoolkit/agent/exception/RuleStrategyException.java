package org.ctoolkit.agent.exception;

/**
 * Exception thrown when no rule strategy is found for migration
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyException
        extends RuntimeException
{
    public RuleStrategyException()
    {
    }

    public RuleStrategyException( String message )
    {
        super( message );
    }
}
