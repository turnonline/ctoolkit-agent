package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Transformer transforms date into predefined format
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DateTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyDateTransformer>
{
    private static Logger log = LoggerFactory.getLogger( DateTransformerProcessor.class );

    @Override
    public Object transform( Object value, MigrationSetPropertyDateTransformer transformer )
    {
        Date date = null;

        if ( value instanceof Date )
        {
            date = ( Date ) value;

            if (!transformer.getEpoch())
            {
                SimpleDateFormat sdf = new SimpleDateFormat( transformer.getFormat() );
                sdf.setTimeZone( TimeZone.getTimeZone( transformer.getTimeZone() ) );
                value = sdf.format( value );
            }
        }
        else if ( value instanceof String )
        {
            String format = transformer.getFormat();
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat( format );
                sdf.setTimeZone( TimeZone.getTimeZone( transformer.getTimeZone() ) );
                value = sdf.parse( ( String ) value );
                date = ( Date ) value;
            }
            catch ( ParseException e )
            {
                log.info( "Unable to parse value '" + value + "' from format '" + format + "' as date", e );
            }
        }

        if ( date != null && transformer.getEpoch() )
        {
            return date.getTime();
        }

        return value;
    }
}
