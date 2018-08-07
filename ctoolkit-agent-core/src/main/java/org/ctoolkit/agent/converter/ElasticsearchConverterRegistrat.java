package org.ctoolkit.agent.converter;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;

/**
 * Converter registrat for elasticsearch
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class ElasticsearchConverterRegistrat
        extends ConverterRegistrat
{
    public static final String TYPE_TEXT = "text";

    public static final String TYPE_KEYWORD = "keyword";

    public static final String TYPE_LONG = "long";

    public static final String TYPE_DOUBLE = "double";

    public static final String TYPE_DATE = "date";

    public static final String TYPE_BOOLEAN = "boolean";

    public static final String TYPE_BINARY = "binary";

    @Override
    public void initialize()
    {
        register( String.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Integer.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Long.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Float.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Double.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( BigDecimal.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Boolean.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( byte[].class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Clob.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Blob.class, TYPE_TEXT, StringConverter.INSTANCE );
        register( Date.class, TYPE_TEXT, StringConverter.INSTANCE );

        register( String.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Integer.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Long.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Float.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Double.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( BigDecimal.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Boolean.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( byte[].class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Clob.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Blob.class, TYPE_KEYWORD, StringConverter.INSTANCE );
        register( Date.class, TYPE_KEYWORD, StringConverter.INSTANCE );

        register( String.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Integer.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Long.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Date.class, TYPE_LONG, LongConverter.INSTANCE );

        register( String.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Integer.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Long.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Float.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Double.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( BigDecimal.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Date.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );

        register( String.class, TYPE_DATE, DateConverter.INSTANCE );
        register( Long.class, TYPE_DATE, DateConverter.INSTANCE );
        register( Date.class, TYPE_DATE, DateConverter.INSTANCE );

        register( String.class, TYPE_BOOLEAN, BooleanConverter.INSTANCE );
        register( Boolean.class, TYPE_BOOLEAN, BooleanConverter.INSTANCE );

        register( String.class, TYPE_BINARY, BinaryConverter.INSTANCE );
        register( byte[].class, TYPE_BINARY, BinaryConverter.INSTANCE );
        register( Blob.class, TYPE_BINARY, BinaryConverter.INSTANCE );
        register( Clob.class, TYPE_BINARY, BinaryConverter.INSTANCE );
    }
}
