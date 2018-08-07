package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;

/**
 * Boolean converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BooleanConverter
        extends BaseConverter
{
    public static BooleanConverter INSTANCE = new BooleanConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Object transformedValue = transform( source, property.getTransformers() );
        Boolean target = Boolean.valueOf( transformedValue.toString() );

        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( String.valueOf( target ) );

        return importSetProperty;
    }
}
