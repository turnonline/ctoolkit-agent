package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;

/**
 * String converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class StringConverter
        extends BaseConverter
{
    public static StringConverter INSTANCE = new StringConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Object transformedValue = transform( source, property.getTransformers() );
        String target = String.valueOf( transformedValue );
        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( target );

        return importSetProperty;
    }
}
