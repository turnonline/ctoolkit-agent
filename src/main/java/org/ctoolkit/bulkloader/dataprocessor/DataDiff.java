package org.ctoolkit.bulkloader.dataprocessor;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;

/**
 * Bean holds the data model difference between the current version
 * and the last export (stored in export store)
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface DataDiff
{

    /**
     * Method returns the next ChangeSet diff. It can be called more than once
     * every time you call the method you get the next chunk of differences If
     * there isn't any, the return value is null
     *
     * @return change set between the current data store and the last export store
     * export
     */
    public ChangeSet getNextDiff();
}
