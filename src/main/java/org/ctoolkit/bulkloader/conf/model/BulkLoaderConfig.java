package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root
public class BulkLoaderConfig
{

    @Element( name = "changesets-per-task" )
    private Long csPerTask;

    // upgrade process related constants
    // import related constants
    // export related constants

    public BulkLoaderConfig()
    {
    }

    /**
     * @return the csPerTask
     */
    public Long getCsPerTask()
    {
        return csPerTask;
    }

    /**
     * @param csPerTask the csPerTask to set
     */
    public void setCsPerTask( Long csPerTask )
    {
        this.csPerTask = csPerTask;
    }
}
