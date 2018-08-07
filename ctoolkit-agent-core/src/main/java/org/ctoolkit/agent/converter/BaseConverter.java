package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
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
    private static Map<String, TransformerProcessor> transformerProcessors = new HashMap<>();

    static
    {
        transformerProcessors.put( MapperTransformerProcessor.TYPE, new MapperTransformerProcessor() );
        transformerProcessors.put( DateTransformerProcessor.TYPE, new DateTransformerProcessor() );
        transformerProcessors.put( BlobTransformerProcessor.TYPE, new BlobTransformerProcessor() );
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
            TransformerProcessor processor = transformerProcessors.get( transformer.getType() );
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
