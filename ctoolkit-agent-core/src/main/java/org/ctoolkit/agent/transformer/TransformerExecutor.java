package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyEncodingTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformer executor is used to apply {@link MigrationSetPropertyTransformer} via {@link TransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class TransformerExecutor
{
    private static Map<Class, TransformerProcessor> transformerProcessors = new HashMap<>();

    static
    {
        transformerProcessors.put( MigrationSetPropertyMapperTransformer.class, new MapperTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyDateTransformer.class, new DateTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyBlobTransformer.class, new BlobTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyPatternTransformer.class, new PatternTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyEncodingTransformer.class, new EncodingTransformerProcessor() );
    }

    @SuppressWarnings( "unchecked" )
    public <T> T transform( Object value, List<MigrationSetPropertyTransformer> transformers, Map<Object, Object> ctx, String phase )
    {
        for ( MigrationSetPropertyTransformer transformer : transformers )
        {
            if ( transformer.getPhase().equals( phase ) )
            {
                TransformerProcessor processor = transformerProcessors.get( transformer.getClass() );
                if ( processor != null )
                {
                    value = processor.transform( value, transformer, ctx );
                }
            }
        }

        return (T) value;
    }
}
