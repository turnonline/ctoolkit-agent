package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.Mocks;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link PatternTransformerProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class PatternTransformerProcessorTest
{
    private PatternTransformerProcessor processor = new PatternTransformerProcessor();

    @Test
    public void transform_WithPrefix()
    {
        MigrationSetPropertyPatternTransformer transformer = new MigrationSetPropertyPatternTransformer();
        transformer.setPattern( "client-person:person:{target.value}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( Mocks.migrationSetSource( "global", "Person" ) );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        HashMap<Object, Object> ctx = new HashMap<>();
        ctx.put( MigrationSet.class, migrationSet );

        assertEquals( "client-person:person:1", processor.transform( "1", transformer, ctx ) );
    }

    @Test
    public void transform_WithContext()
    {
        MigrationSetPropertyPatternTransformer transformer = new MigrationSetPropertyPatternTransformer();
        transformer.setPattern( "{target.namespace}:{target.kind}:{target.value}" );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( Mocks.migrationSetSource( "global", "Person" ) );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        HashMap<Object, Object> ctx = new HashMap<>();
        ctx.put( MigrationSet.class, migrationSet );

        assertEquals( "client-person:person:1", processor.transform( "1", transformer, ctx ) );
    }
}