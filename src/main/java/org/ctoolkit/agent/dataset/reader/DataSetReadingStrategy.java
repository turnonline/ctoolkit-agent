package org.ctoolkit.agent.dataset.reader;


import org.ctoolkit.agent.dataset.ChangeSet;

import java.util.Iterator;

/**
 * Data set reading strategy iterator interface.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface DataSetReadingStrategy
        extends Iterator<ChangeSet>
{
    public String getCursor();
}
