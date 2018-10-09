package org.ctoolkit.agent.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter registrat base class
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class BaseConverterRegistrat
        implements ConverterRegistrat
{
    private Map<Converter.Key, Converter> converters = new HashMap<>();

    public BaseConverterRegistrat()
    {
        initialize();
    }

    public abstract void initialize();

    @Override
    public void register( Class source, String target, Converter converter )
    {
        converters.put( converter.key( source, target ), converter );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Converter get( Object sourceValue, String targetTypeName )
    {
        for ( Map.Entry<Converter.Key, Converter> entry : converters.entrySet() )
        {
            Class sourceClassName = entry.getKey().getSourceClassName();
            String targetType = entry.getKey().getTargetTypeName();

            if ( sourceClassName.isAssignableFrom( sourceValue.getClass() ) && targetTypeName.equals( targetType ) )
            {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public Converter get( String targetTypeName )
    {
        for ( Map.Entry<Converter.Key, Converter> entry : converters.entrySet() )
        {
            String targetType = entry.getKey().getTargetTypeName();

            if ( targetTypeName.equals( targetType ) )
            {
                return entry.getValue();
            }
        }

        return null;
    }
}
