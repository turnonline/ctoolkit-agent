package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformerMappings;

/**
 * Implementation of {@link MigrationSetPropertyMapperTransformer} transformer
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MapperTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyMapperTransformer>
{
    public static final String TYPE = "mapper";

    @Override
    public Object transform( Object value, MigrationSetPropertyMapperTransformer transformer )
    {
        for ( MigrationSetPropertyMapperTransformerMappings mapping : transformer.getMappings() )
        {
            if ( mapping.getSource().equals( value ) )
            {
                return mapping.getTarget();
            }
        }

        return value;
    }
}
