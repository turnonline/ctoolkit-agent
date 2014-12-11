package org.ctoolkit.bulkloader.changesets;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ChangeSetVersionModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        install( new FactoryModuleBuilder()
                .implement( ChangeSetReadingStrategy.class, Names.named( "DataSets" ), DataSetsChangeSetReadingStrategy.class )
                .implement( ChangeSetReadingStrategy.class, Names.named( "DataStore" ), DataStoreChangeSetReadingStrategy.class )
                .implement( ChangeSetReadingStrategy.class, Names.named( "ExportStore" ), ExportStoreChangeSetReadingStrategy.class )
                .build( ChangeSetReadingStrategyFactory.class ) );

        bind( ChangeSetVersionService.class ).to( ChangeSetVersionImpl.class ).in( Singleton.class );
    }
}
