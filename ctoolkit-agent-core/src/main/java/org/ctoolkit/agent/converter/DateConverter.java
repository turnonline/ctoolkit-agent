package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Date converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DateConverter
        extends BaseConverter
{
    private static final Logger log = LoggerFactory.getLogger( DateConverter.class );

    public static DateConverter INSTANCE = new DateConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Object transformedValue = transform( source, property.getTransformers() );
        Date target = null;
        try
        {
            if ( transformedValue instanceof Date )
            {
                target = ( Date ) transformedValue;
            }
            else
            {
                target = new Date( Long.valueOf( transformedValue.toString() ) );
            }
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create date from value: '" + source + "'", e );
        }
        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( target != null ? Long.valueOf( target.getTime() ).toString() : null );

        return importSetProperty;
    }
}
