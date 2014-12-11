package org.ctoolkit.bulkloader.datastore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;

/**
 * Callback interface for entity operations
 * Callback is called from data store, when an operation is done
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface DataStoreCallback
{

    /**
     * Method called when an entity operation is done by the DataStore
     *
     * @param cse entity operation done
     */
    void entityOpDone( final ChangeSetEntity cse );
}
