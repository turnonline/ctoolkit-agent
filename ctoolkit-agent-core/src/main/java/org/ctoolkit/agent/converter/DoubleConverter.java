package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Double converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DoubleConverter
        extends BaseConverter
{
    private static final Logger log = LoggerFactory.getLogger( DoubleConverter.class );

    public static DoubleConverter INSTANCE = new DoubleConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Object transformedValue = transform( source, property.getTransformers() );
        Double target = null;
        try
        {
            target = new Double( transformedValue.toString() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create double from value: '" + source + "'", e );
        }
        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( String.valueOf( target ) );

        return importSetProperty;
    }
}
