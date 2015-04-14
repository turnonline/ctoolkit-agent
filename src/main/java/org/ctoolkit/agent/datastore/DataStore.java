package org.ctoolkit.agent.datastore;

import com.google.appengine.api.taskqueue.DeferredTask;
import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.processor.DataSet;

/**
 * The datastore interface as an abstraction over potential many underlying datastores.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface DataStore
{
    /**
     * Registers datastore callback to receive events on datastore operations.
     *
     * @param callback the callback to be registered
     */
    public void registerCallback( final DataStoreCallback callback );

    /**
     * Method for applying change set on the data store
     *
     * @param changeSet change set to apply, update progress parameter hold cursors
     *                  into this change set
     * @throws AgentException if something went wrong
     */
    public void applyChangeSet( final ChangeSet changeSet ) throws AgentException;

    /**
     * Resets the internal pointer to the first full exported change set
     * with version less or equal with upToVersion
     *
     * @param kind   the entity kind
     * @param cursor the cursor position
     */
    public void setFirstChangeSet( String kind, String cursor );

    /**
     * Returns the next ChangeSet. It can be called more than once every
     * time you call the method you get the next change set. If there isn't any
     * other, the return value will be null. When the method reaches the last entity
     * for the current kind, the return value is null.
     *
     * @return the change set instance or null
     */
    public ChangeSet getNextChangeSet();

    /**
     * Returns the current cursor position from data store
     * The current kind is set by {@link #setFirstChangeSet(String, String)}
     *
     * @return the cursor in data store
     */
    public String getCursor();

    /**
     * Return data set for given Id.
     * <p>
     * Note: there is a specific built-in data set returned for Id = 0 to process data sets from local file system
     * under '/dataset' directory.
     *
     * @param id the Id of the data set to be retrieved
     * @return the data set instance for given Id
     */
    DataSet getDataSet( Long id );

    /**
     * Adds the task in to queued to be executed at some time in the near future.
     *
     * @param runnable the runnable task to be executed
     */
    void addToQueue( DeferredTask runnable );
}
