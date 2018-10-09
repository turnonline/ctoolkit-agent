package org.ctoolkit.agent.converter;

import org.apache.beam.repackaged.beam_runners_core_java.com.google.common.io.BaseEncoding;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyEncodingTransformer;
import org.ctoolkit.agent.transformer.BlobTransformerProcessor;
import org.ctoolkit.agent.transformer.EncodingTransformerProcessor;

import java.util.HashMap;

/**
 * Binary converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BinaryConverter
        implements Converter
{
    public static BinaryConverter INSTANCE = new BinaryConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        MigrationSetPropertyEncodingTransformer encodingTransformer = new MigrationSetPropertyEncodingTransformer();
        encodingTransformer.setEncodingType( "base64" );
        EncodingTransformerProcessor encodingProcessor = new EncodingTransformerProcessor();

        MigrationSetPropertyBlobTransformer blobTransformer = new MigrationSetPropertyBlobTransformer();
        BlobTransformerProcessor blobProcessor = new BlobTransformerProcessor();

        // convert blob to String
        Object target = blobProcessor.transform( source, blobTransformer, new HashMap<>(  ) );

        // convert String to base64 string
        target = encodingProcessor.transform( target, encodingTransformer, new HashMap<>(  ) );

        return target.toString();
    }

    @Override
    public byte[] convert( ImportSetProperty property )
    {
        return BaseEncoding.base64().decode( property.getValue() );
    }
}
