package org.ctoolkit.bulkloader.common;

import org.ctoolkit.bulkloader.dataprocessor.DataProcessor;

import javax.inject.Inject;

/**
 * Implementation of the bulk loader
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderImpl
        implements BulkLoader
{

    /**
     * Data processor
     */
    private final DataProcessor dataProcessor;

    @Inject
    protected BulkLoaderImpl( DataProcessor dataProcessor )
    {
        this.dataProcessor = dataProcessor;
    }

    /**
     * Calling the import with input parameters
     *
     * @param progressInfo progress info from previous step
     * @return bean holding the import process result
     * @throws BulkLoaderException if something went wrong
     */
    @Override
    public BulkLoaderProgressInfo doImport( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException
    {
        return dataProcessor.importVersionFromExportStore( progressInfo );
    }

    /**
     * Calling the update with input parameters
     *
     * @param progressInfo progress info from previous step
     * @return bean holding the export process result
     * @throws BulkLoaderException if something went wrong
     */
    @Override
    public BulkLoaderProgressInfo doUpgrade( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException
    {
        return dataProcessor.upgradeToVersion( progressInfo );
    }

    /**
     * Calling the update with input parameters
     *
     * @param progressInfo progress info from previous step
     * @return bean holding the export process result
     * @throws BulkLoaderException if something went wrong
     */
    @Override
    public BulkLoaderProgressInfo doExport( BulkLoaderProgressInfo progressInfo ) throws BulkLoaderException
    {
        return dataProcessor.exportCurrentVersion( progressInfo );
    }
}
