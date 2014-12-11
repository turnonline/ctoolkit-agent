package org.ctoolkit.bulkloader.changesets;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ChangeSetReadingStrategy
{

    public boolean hasNext();

    public ChangeSet next();

    public String getCursor();
}
