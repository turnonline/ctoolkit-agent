package org.ctoolkit.bulkloader.datastore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.common.BulkLoaderException;

/**
 * Interface for datastore
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface DataStore
{

    /**
     * Method registers callback for data store operations (entityOpDone)
     *
     * @param datastoreCallback callback method to register
     */
    public void registerCallback( final DataStoreCallback datastoreCallback );

    /**
     * Method for applying changeset on the data store
     *
     * @param changeSet changeset to apply, updateprogress parameter hold cursors
     *                  into this changeset
     * @throws org.ctoolkit.bulkloader.common.BulkLoaderException if something went wrong
     */
    public void applyChangeSet( final ChangeSet changeSet ) throws BulkLoaderException;

    /**
     * Returns a changeset holding the whole datastore
     *
     * @return datastore
     */
    // TODO: implement iterating trough datastore, setFirst(), getNext()...
    public ChangeSet getCurrentVersion();

    /**
     * Method resets the internal pointer to the first full exported change set
     * with version less or equal with upToVersion
     *
     * @param kind
     * @param cursor
     */
    public void setFirstChangeSet( String kind, String cursor );

    /**
     * Method returns the next ChangeSet. It can be called more than once every
     * time you call the method you get the next change set. If there isn't any
     * other, the return value is null. When the method reaches the last entity
     * for the current kind, the return value is null.
     *
     * @return ChangeSet or null
     */
    public ChangeSet getNextChangeSet();

    /**
     * Method return the current cursor position from data store
     * The current kind is set by setFirstChangeSet
     *
     * @return cursor in data store
     */
    public String getCursor();

    /**
     * Method returns basic information about the Export store
     *
     * @return data store information
     */
    public String getInfo();

}
