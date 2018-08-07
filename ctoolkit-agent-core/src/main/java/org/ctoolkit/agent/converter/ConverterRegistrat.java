package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter registrat base class
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class ConverterRegistrat
{
    private Map<Converter.Key, Converter> converters = new HashMap<>();

    public ConverterRegistrat()
    {
        initialize();
    }

    public abstract void initialize();

    public void register( Class source, String target, Converter converter )
    {
        converters.put( converter.key( source, target ), converter );
    }

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

    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        Converter converter = get( source, property.getTargetType() );
        if ( converter != null )
        {
            return converter.convert( source, property );
        }

        return null;
    }
}
