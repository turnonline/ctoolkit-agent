package org.ctoolkit.agent.model;

import com.google.cloud.datastore.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper is used to maps native database types to agent types
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class NativeToAgentTypeMapper
{
    private static final String UNKNOWN_TYPE = "unknown";

    public static NativeToAgentTypeMapper INSTANCE = new NativeToAgentTypeMapper();

    private Map<String, SimpleTypeResolver> resolvers = new HashMap<>();

    public NativeToAgentTypeMapper()
    {
        register( "NULL", SimpleTypeResolver.INSTANCE );
        register( "STRING", SimpleTypeResolver.INSTANCE );
        register( "INT64", SimpleTypeResolver.INSTANCE );
        register( "LONG", SimpleTypeResolver.INSTANCE );
        register( "DOUBLE", SimpleTypeResolver.INSTANCE );
        register( "BOOLEAN", SimpleTypeResolver.INSTANCE );
        register( "TIMESTAMP", SimpleTypeResolver.INSTANCE );
        register( "REFERENCE", SimpleTypeResolver.INSTANCE );
    }

    public String toAgent( Value<?> value )
    {
        String type = value.get().toString();
        if ( resolvers.containsKey( type ) )
        {
            return resolvers.get( type ).resolveValue( value );
        }

        return UNKNOWN_TYPE;
    }

    public void register( String nativeType, SimpleTypeResolver resolver )
    {
        resolvers.put( nativeType, resolver );
    }

    public static class SimpleTypeResolver
    {
        static SimpleTypeResolver INSTANCE = new SimpleTypeResolver();

        private static Map<String, String> diffMap = new HashMap<>();

        static
        {
            diffMap.put( "INT64", "LONG" );
            diffMap.put( "TIMESTAMP", "DATE" );
        }

        String resolveValue( Value<?> value )
        {
            String val = value.get().toString();
            if ( diffMap.containsKey( val ) )
            {
                val = diffMap.get( val );
            }

            return val.toLowerCase();
        }
    }
}
