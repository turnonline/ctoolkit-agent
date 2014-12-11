package org.ctoolkit.bulkloader.changesets;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ChangeSetReader
{

    private ChangeSetReadingStrategy readingStrategy;

    // Constructor
    public ChangeSetReader( ChangeSetReadingStrategy readingStrategy )
    {
        this.readingStrategy = readingStrategy;
    }

    public boolean hasNext()
    {
        return readingStrategy.hasNext();
    }

    public ChangeSet next()
    {
        return readingStrategy.next();
    }

    public String getCursor()
    {
        return readingStrategy.getCursor();
    }
}
