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
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( LongConverter.class );

    public static LongConverter INSTANCE = new LongConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        Long target = null;
        try
        {
            target = new Long( source.toString() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create long from value: '" + source + "'", e );
        }

        return target != null ? target.toString() : null;
    }

    @Override
    public Long convert( ImportSetProperty property )
    {
        Long target = null;
        try
        {
            target = new Long( property.getValue() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create long from value: '" + property.getValue() + "'", e );
        }

        return target;
    }
}
