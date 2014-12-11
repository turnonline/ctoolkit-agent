package org.ctoolkit.bulkloader.undostore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSets;

/**
 * Stores entries in the diary Used for logging successful operations
 * in the data store.
 * Content can be used for UNDO operation
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ProgressDiary
{

    /**
     * An entry was written into data store.
     *
     * @param changeSet changeset containing written entry
     */
    public void writeEntry( final ChangeSet changeSet );

    /**
     * Return entries from the diary
     *
     * @return
     */
    public ChangeSets readEntries();

    /**
     * Remove entries from the undo store
     */
    public void purgeEntries();

    /**
     * Method makes a rollback on the undo entries The newly created entries
     * gonna be removed
     */
    public void rollback();

    /**
     * True if there are any records in the diary
     *
     * @return
     */
    public boolean isEmpty();

    /**
     * Method returns info about undo store
     *
     * @return info about info store
     */
    public String getInfo();
}
