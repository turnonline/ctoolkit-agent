package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link DateTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DateTransformerProcessorTest
{
    private DateTransformerProcessor processor = new DateTransformerProcessor();

    @Test
    public void transform_DateToString()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "dd-MM-yyyy" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertEquals( "01-01-2018", processor.transform( calendar.getTime(), transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_DateToEpoch()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setEpoch( true );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertEquals( 1514764800000L, processor.transform( calendar.getTime(), transformer, new HashMap<>() ) );
    }

    @Test
    public void transform_StringParseError()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "dd-MM-yyyy" );

        Object date = processor.transform( "1fd", transformer, new HashMap<>() );
        assertEquals( "1fd", date );
    }

    @Test
    public void transform_StringToDate()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "dd-MM-yyyy" );

        Date date = ( Date ) processor.transform( "01-01-2018", transformer, new HashMap<>() );
        assertEquals( 1514764800000L, date.getTime() );
    }

    @Test
    public void transform_StringToEpoch()
    {
        MigrationSetPropertyDateTransformer transformer = new MigrationSetPropertyDateTransformer();
        transformer.setFormat( "dd-MM-yyyy" );
        transformer.setEpoch( true );

        assertEquals( 1514764800000L, processor.transform( "01-01-2018", transformer, new HashMap<>() ) );
    }
}