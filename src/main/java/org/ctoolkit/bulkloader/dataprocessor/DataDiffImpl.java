package org.ctoolkit.bulkloader.dataprocessor;

import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;
import org.ctoolkit.bulkloader.datastore.DataStore;

import javax.inject.Inject;

/**
 * Implementation of the DataModelDiff interface
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class DataDiffImpl
        implements DataDiff
{

    /**
     * Global configuration
     */
    private final DataStore dataStore;

    private final ChangeSetVersion currVersion;

    /**
     * Cursor into data store. The next call to getNextDiff will return the next
     * data from this position. It points to the item, which is already read
     */
    private long cursor;

    @Inject
    protected DataDiffImpl( DataStore ds, ChangeSetVersionService vs )
    {
        dataStore = ds;
        currVersion = vs.getCurrentChangeSetVersion();
        cursor = -1;
    }

    /**
     * Method returns the next ChangeSet diff. It can be called more than once
     * every time you call the method you get the next chunk of differences.
     *
     * @return change set between the current data store and the last blob store
     * export
     */
    @Override
    public ChangeSet getNextDiff()
    {
        // cursor = -1;
        if ( ++cursor > 0 )
        {
            // simulate the last item
            cursor = -1;
            return ChangeSet.createChangeSet( currVersion.getVersion(), currVersion.getUser() );
        }
        cursor = 0;
        return getFullDiff();
    }

    /**
     * Method returns a full ChangeSet diff.
     *
     * @return full dataset diff from current version
     */
    private ChangeSet getFullDiff()
    {
        return dataStore.getCurrentVersion();
    }
}
