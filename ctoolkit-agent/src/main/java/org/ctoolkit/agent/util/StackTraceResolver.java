package org.ctoolkit.agent.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility for resolving stack trace from exception
 * <p>
 * Created by Jozef Pohorelec on 6. 8. 2015.
 */
public final class StackTraceResolver
{
    private StackTraceResolver()
    {
    }

    /**
     * Resolve error stack trace
     *
     * @param e exception to resolve
     * @return stack trace as string
     */
    public static String resolve( Throwable e )
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw );

        e.printStackTrace( pw );

        return sw.toString();
    }
}
