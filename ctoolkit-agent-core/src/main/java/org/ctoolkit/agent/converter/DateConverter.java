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
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( DateConverter.class );

    public static DateConverter INSTANCE = new DateConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        Date target = null;
        try
        {
            if ( source instanceof Date )
            {
                target = ( Date ) source;
            }
            else
            {
                target = new Date( Long.valueOf( source.toString() ) );
            }
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create date from value: '" + source + "'", e );
        }

        return target != null ? Long.valueOf( target.getTime() ).toString() : null;
    }

    @Override
    public Date convert( ImportSetProperty property )
    {
        Date target = null;
        try
        {
            target = new Date( Long.valueOf( property.getValue() ) );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create date from value: '" + property.getValue() + "'", e );
        }

        return target;
    }
}
