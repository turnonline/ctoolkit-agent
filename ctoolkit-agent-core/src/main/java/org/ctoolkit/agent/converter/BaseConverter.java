package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;
import org.ctoolkit.agent.transformer.BlobTransformerProcessor;
import org.ctoolkit.agent.transformer.DateTransformerProcessor;
import org.ctoolkit.agent.transformer.MapperTransformerProcessor;
import org.ctoolkit.agent.transformer.TransformerProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for {@link Converter} superclasses
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class BaseConverter
        implements Converter
{
    private static Map<Class, TransformerProcessor> transformerProcessors = new HashMap<>();

    static
    {
        transformerProcessors.put( MigrationSetPropertyMapperTransformer.class, new MapperTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyDateTransformer.class, new DateTransformerProcessor() );
        transformerProcessors.put( MigrationSetPropertyBlobTransformer.class, new BlobTransformerProcessor() );
    }

    @Override
    public Key key( Class source, String target )
    {
        return new Key( source, target );
    }

    @SuppressWarnings( "unchecked" )
    protected Object transform( Object value, List<MigrationSetPropertyTransformer> transformers )
    {
        for ( MigrationSetPropertyTransformer transformer : transformers )
        {
            TransformerProcessor processor = transformerProcessors.get( transformer.getClass() );
            if ( processor != null )
            {
                value = processor.transform( value, transformer );
            }
        }

        return value;
    }

    protected ImportSetProperty newImportSetProperty( MigrationSetProperty property )
    {
        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( property.getTargetProperty() );
        importSetProperty.setType( property.getTargetType() );

        return importSetProperty;
    }
}
