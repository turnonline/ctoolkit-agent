package org.ctoolkit.bulkloader.dataprocessor;

import org.ctoolkit.bulkloader.common.BulkLoaderException;
import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface DataProcessor
{
    /**
     * Method imports version from export store. It goes from current version of
     * the data store to the requested version version.
     *
     * @param progressInfo progress info
     * @return
     */
    public BulkLoaderProgressInfo importVersionFromExportStore( BulkLoaderProgressInfo progressInfo )
            throws BulkLoaderException;

    /**
     * Method reads change sets from change set file and applies them one by one on
     * the data store
     *
     * @param progressInfo progress info
     * @return
     */
    public BulkLoaderProgressInfo upgradeToVersion( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException;

    /**
     * Method used by roll back. It looks for last full dataStore dump in the
     * export store, then goes trough export store/user version (with priority
     * on export store) and imports them one by one
     *
     * @param version version to rebuild
     * @return
     */
    public void rebuildVersion( Long version ) throws BulkLoaderException;

    /**
     * Method exports the current version and stores it in blob store
     *
     * @return
     */
    public BulkLoaderProgressInfo exportCurrentVersion( BulkLoaderProgressInfo progressInfo )
            throws BulkLoaderException;
}
