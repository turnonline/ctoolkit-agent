package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Double converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DoubleConverter
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( DoubleConverter.class );

    public static DoubleConverter INSTANCE = new DoubleConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        Double target = null;
        try
        {
            target = new Double( source.toString() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create double from value: '" + source + "'", e );
        }

        return String.valueOf( target );
    }
}
