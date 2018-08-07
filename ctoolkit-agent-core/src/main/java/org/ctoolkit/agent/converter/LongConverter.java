package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Long converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class LongConverter
        extends BaseConverter
{
    private static final Logger log = LoggerFactory.getLogger( LongConverter.class );

    public static LongConverter INSTANCE = new LongConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Object transformedValue = transform( source, property.getTransformers() );
        Long target = null;
        try
        {
            target = new Long( transformedValue.toString() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create long from value: '" + source + "'", e );
        }
        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( String.valueOf( target ) );

        return importSetProperty;
    }
}
