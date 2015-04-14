package org.ctoolkit.agent.dataset.reader;


import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.processor.ChangeSetVersion;

/**
 * The service handling meta-data of the current state of the datastore schema.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface ChangeSetVersionService
{
    /**
     * Returns the current meta-data of the datastore schema.
     * @return an instance of the {@link org.ctoolkit.agent.dataset.processor.ChangeSetVersion} class.
     */
    ChangeSetVersion getCurrentChangeSetVersion();

    /**
     * Updates the meta-data of the datastore schema.
     *
     * @param version a change set version value
     */
    void saveChangeSetVersionInfo( ChangeSetVersion version );

    /**
     * Updates the meta-data of the datastore schema.
     *
     * @param changeSet the change set as source of the current version to update
     */
    void saveChangeSetVersionInfo( ChangeSet changeSet );
}
