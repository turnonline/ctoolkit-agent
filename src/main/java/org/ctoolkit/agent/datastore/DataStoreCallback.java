package org.ctoolkit.agent.datastore;


import org.ctoolkit.agent.dataset.ChangeSetEntity;

/**
 * The callback interface for entity operations. It will be called from data store, once an operation has done.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface DataStoreCallback
{

    /**
     * Method called once an operation has done.
     *
     * @param cse entity operation done
     */
    void entityOperationDone( final ChangeSetEntity cse );
}
