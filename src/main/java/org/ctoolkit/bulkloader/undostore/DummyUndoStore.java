package org.ctoolkit.bulkloader.undostore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSets;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class DummyUndoStore
        implements ProgressDiary
{
    @Override
    public void writeEntry( ChangeSet changeSet )
    {
    }

    @Override
    public ChangeSets readEntries()
    {
        return new ChangeSets();
    }

    @Override
    public void purgeEntries()
    {
    }

    @Override
    public void rollback()
    {
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public String getInfo()
    {
        return "";
    }
}
