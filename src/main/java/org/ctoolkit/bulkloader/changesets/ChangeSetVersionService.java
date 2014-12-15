package org.ctoolkit.bulkloader.changesets;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;


/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ChangeSetVersionService
{
    /**
     * Method return the current version of the data model
     */
    public ChangeSetVersion getCurrentChangeSetVersion();

    /**
     * Method updates the data model version
     */
    public void saveChangeSetVersionInfo( ChangeSetVersion version );

    /**
     * Method updates the data model version
     */
    public void saveChangeSetVersionInfo( ChangeSet changeSet );
}
