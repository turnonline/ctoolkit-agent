package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.MigrationSetProperty;

/**
 * String converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class StringConverter
        implements Converter
{
    public static StringConverter INSTANCE = new StringConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        return String.valueOf( source );
    }
}
