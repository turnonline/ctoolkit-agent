package org.ctoolkit.bulkloader.dataprocessor;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionImpl;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.datastore.DataStoreModule;

public class DataProcessorModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        install( new DataStoreModule() );
        bind( ChangeSetVersionService.class ).to( ChangeSetVersionImpl.class ).in( Singleton.class );
        bind( DataDiff.class ).to( DataDiffImpl.class );
        bind( DataProcessor.class ).to( DataProcessorImpl.class ).in( Singleton.class );
    }
}
