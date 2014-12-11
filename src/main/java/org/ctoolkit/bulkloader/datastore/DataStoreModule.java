package org.ctoolkit.bulkloader.datastore;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.ctoolkit.bulkloader.datastore.bigtable.BigTableDataStore;
import org.ctoolkit.bulkloader.exportstore.ExportStore;
import org.ctoolkit.bulkloader.exportstore.bigtable.BigTableExportStore;
import org.ctoolkit.bulkloader.undostore.DummyUndoStore;
import org.ctoolkit.bulkloader.undostore.ProgressDiary;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class DataStoreModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( ProgressDiary.class ).to( DummyUndoStore.class ).in( Singleton.class );
        bind( DataStore.class ).to( BigTableDataStore.class ).in( Singleton.class );
        bind( ExportStore.class ).to( BigTableExportStore.class ).in( Singleton.class );
    }
}
