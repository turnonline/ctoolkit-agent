package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Object holds list of Changeset entries
 */
@Root
public class ChangeSets
{

    /**
     * List of changesets
     */
    @ElementList( inline = true, required = false )
    private List<ChangeSet> changeSets;

    /**
     * Default constructor
     */
    public ChangeSets()
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
