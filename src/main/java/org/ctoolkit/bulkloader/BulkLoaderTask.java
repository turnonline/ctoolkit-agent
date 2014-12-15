package org.ctoolkit.bulkloader;

import org.ctoolkit.bulkloader.common.BulkLoaderProgressInfo;

/**
 *
 */
public abstract class BulkLoaderTask
        extends com.comvai.services.task.Task
{
    protected BulkLoaderProgressInfo progressInfo;

    public BulkLoaderTask( String name, BulkLoaderProgressInfo progressInfo )
    {
        super( name );

        this.progressInfo = progressInfo;
    }
}
