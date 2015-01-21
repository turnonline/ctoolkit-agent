package org.ctoolkit.agent.dataset;

import java.util.List;

/**
 * The list of {@link ChangeSet} entries.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DataSet
{

    /**
     * List of change set
     */
    private List<ChangeSet> changeSets;

    /**
     * Default constructor
     */
    public DataSet()
    {
    }

    /**
     * @return the changeSets
     */
    public List<ChangeSet> getChangeSets()
    {
        return changeSets;
    }

    /**
     * @param changeSets the changeSets to set
     */
    public void setChangeSets( List<ChangeSet> changeSets )
    {
        this.changeSets = changeSets;
    }
}
