package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.Mocks;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.transformer.TransformerExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link ConverterExecutor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class ConverterExecutorTest
{
    @Mock
    private ConverterRegistrat registrat;

    private ConverterExecutor executor = new ConverterExecutor(new TransformerExecutor(), registrat);

    // -- convert id

    @Test
    public void testConvertId_Plain()
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setEncodeId( false );

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( source );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        String converted = executor.convertId( migrationSet, Mocks.exportData( "id", 1L ) );
        assertEquals( "client-person:person:1", converted );
    }

    @Test
    public void testConvertId_EncodeBase64()
    {
        MigrationSetSource source = new MigrationSetSource();

        MigrationSet migrationSet = new MigrationSet();
        migrationSet.setSource( source );
        migrationSet.setTarget( Mocks.migrationSetTarget( "client-person", "person" ) );

        String converted = executor.convertId( migrationSet, Mocks.exportData( "id", 1L ) );
        assertEquals( "Y2xpZW50LXBlcnNvbjpwZXJzb246MQ==", converted );
    }
}