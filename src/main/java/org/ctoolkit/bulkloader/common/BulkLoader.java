package org.ctoolkit.bulkloader.common;


/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface BulkLoader
{

    /**
     * Calling the import with input parameters
     *
     * @param progressInfo import progress
     * @return bean holding the import process result
     * @throws BulkLoaderException if something went wrong
     */
    public BulkLoaderProgressInfo doImport( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException;

    /**
     * Calling the export with input parameters
     *
     * @param progressInfo export progress
     * @return bean holding the export process result
     * @throws BulkLoaderException if something went wrong
     */
    public BulkLoaderProgressInfo doExport( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException;

    /**
     * Calling the update with input parameters
     *
     * @param progressInfo upgrade progress
     * @return bean holding the update process state
     * @throws BulkLoaderException if something went wrong
     */
    public BulkLoaderProgressInfo doUpgrade( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException;
}
