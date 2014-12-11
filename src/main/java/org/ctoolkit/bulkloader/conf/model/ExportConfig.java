package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root( name = "config" )
public class ExportConfig
{

    @Attribute( required = true )
    private Long maxItemsPerChangeSet;

    @Attribute( required = true )
    private Long maxSecondsPerChangeSet;

    /**
     * @return the maxItemsPerChangeSet
     */
    public Long getMaxItemsPerChangeSet()
    {
        return maxItemsPerChangeSet;
    }

    /**
     * @param maxItemsPerChangeSet the maxItemsPerChangeSet to set
     */
    public void setMaxItemsPerChangeSet( Long maxItemsPerChangeSet )
    {
        this.maxItemsPerChangeSet = maxItemsPerChangeSet;
    }

    /**
     * @return the maxSecondsPerChangeSet
     */
    public Long getMaxSecondsPerChangeSet()
    {
        return maxSecondsPerChangeSet;
    }

    /**
     * @param maxSecondsPerChangeSet the maxSecondsPerChangeSet to set
     */
    public void setMaxSecondsPerChangeSet( Long maxSecondsPerChangeSet )
    {
        this.maxSecondsPerChangeSet = maxSecondsPerChangeSet;
    }
}
