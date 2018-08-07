package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binary converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BinaryConverter
        extends BaseConverter
{
    private static final Logger log = LoggerFactory.getLogger( BinaryConverter.class );

    public static BinaryConverter INSTANCE = new BinaryConverter();

    @Override
    public ImportSetProperty convert( Object source, MigrationSetProperty property )
    {
        if ( !hasBlobTransformer( property ) )
        {
            MigrationSetPropertyBlobTransformer transformer = new MigrationSetPropertyBlobTransformer();
            transformer.setEncodeToBase64( true );
            property.getTransformers().add( 0, transformer );
        }

        Object transformedValue = transform( source, property.getTransformers() );
        String target = transformedValue.toString();
        ImportSetProperty importSetProperty = newImportSetProperty( property );
        importSetProperty.setValue( target );

        return importSetProperty;
    }

    private boolean hasBlobTransformer( MigrationSetProperty property )
    {
        for ( MigrationSetPropertyTransformer transformer : property.getTransformers() )
        {
            if ( transformer instanceof MigrationSetPropertyBlobTransformer )
            {
                return true;
            }
        }

        return false;
    }
}
