package org.ctoolkit.agent;

import io.micronaut.runtime.Micronaut;

/**
 * Ctoolkit agent bootstrap class
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Application
{
    public static void main( String[] args )
    {
        Micronaut.run( Application.class );
    }
}
