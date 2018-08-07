package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformer;
import org.ctoolkit.agent.model.api.MigrationSetPropertyMapperTransformerMappings;
import org.ctoolkit.agent.transformer.MapperTransformerProcessor;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link MapperTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MapperTransformerProcessorTest
{
    private MapperTransformerProcessor processor = new MapperTransformerProcessor();

    @Test
    public void transformMale()
    {
        assertEquals( "MALE", processor.transform( "M", mockTransformer() ) );
    }

    @Test
    public void transformFemale()
    {
        assertEquals( "FEMALE", processor.transform( "F", mockTransformer() ) );
    }

    @Test
    public void transformUnknown()
    {
        assertEquals( "X", processor.transform( "X", mockTransformer() ) );
    }

    private MigrationSetPropertyMapperTransformer mockTransformer()
    {
        MigrationSetPropertyMapperTransformerMappings mappings1 = new MigrationSetPropertyMapperTransformerMappings();
        mappings1.setSource( "M" );
        mappings1.setTarget( "MALE" );

        MigrationSetPropertyMapperTransformerMappings mappings2 = new MigrationSetPropertyMapperTransformerMappings();
        mappings2.setSource( "F" );
        mappings2.setTarget( "FEMALE" );

        MigrationSetPropertyMapperTransformer transformer = new MigrationSetPropertyMapperTransformer();
        transformer.setMappings( new ArrayList<>() );
        transformer.getMappings().add( mappings1 );
        transformer.getMappings().add( mappings2 );

        return transformer;
    }
}