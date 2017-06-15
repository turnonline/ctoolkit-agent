package org.ctoolkit.agent.model;

import com.google.cloud.datastore.Entity;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ModelConverter
{
    public static <T> T convert( Class<T> type, Entity entity )
    {
        if ( Convertible.class.isAssignableFrom( type ) )
        {
            try
            {
                Convertible convertible = ( Convertible ) type.newInstance();
                convertible.convert( entity );
                return ( T ) convertible;
            }
            catch ( InstantiationException | IllegalAccessException e )
            {
                throw new RuntimeException( "Unable to create new instance of type" );
            }
        }

        throw new IllegalArgumentException( "Type is not convertible" );
    }
}
