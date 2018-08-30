package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;

import java.util.Map;

/**
 * Transformer processor is used to implement {@link MigrationSetPropertyTransformer} transform logic
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface TransformerProcessor<TRANSFORMER extends MigrationSetPropertyTransformer>
{
    enum Phase
    {
        PRE_CONVERT( "pre-convert" ),
        POST_CONVERT( "post-convert" );

        private String value;

        Phase( String value )
        {
            this.value = value;
        }

        public static Phase get( String value )
        {
            for ( Phase phase : Phase.values() )
            {
                if ( value.equals( phase.value() ) )
                {
                    return phase;
                }
            }

            throw new IllegalArgumentException( "Phase '" + value + "' not supported. Supported phases are: 'pre-convert', 'post-convert'" );
        }

        public String value()
        {
            return value;
        }
    }

    Object transform( Object value, TRANSFORMER transformer, Map<Object, Object> ctx );
}
