package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Transformer transforms date into predefined format
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
// TODO: write unit test
public class DateTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyDateTransformer>
{
    public static final String CONST_EPOCH = "epoch";

    private static Logger log = LoggerFactory.getLogger( DateTransformerProcessor.class );

    @Override
    public Object transform( Object value, MigrationSetPropertyDateTransformer transformer )
    {
        if ( value instanceof Date )
        {
            String format = transformer.getFormat();
            if ( CONST_EPOCH.equals( format ) )
            {
                return ( ( Date ) value ).getTime();
            }
            else
            {
                return new SimpleDateFormat( format ).format( value );
            }
        }
        else if ( value instanceof String )
        {
            String format = transformer.getFormat();
            try
            {
                return new SimpleDateFormat( format ).parse( ( String ) value ).getTime();
            }
            catch ( ParseException e )
            {
                log.info( "Unable to parse value '" + value + "' from format '" + format + "' as date", e );
            }
        }

        return value;
    }
}
