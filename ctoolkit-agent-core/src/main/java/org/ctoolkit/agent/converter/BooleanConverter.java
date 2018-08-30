package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.MigrationSetProperty;

/**
 * Boolean converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BooleanConverter
        implements Converter
{
    public static BooleanConverter INSTANCE = new BooleanConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        return Boolean.valueOf( source.toString() ).toString();
    }
}
