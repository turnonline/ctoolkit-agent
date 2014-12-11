package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root( name = "config" )
public class ImportConfig
{

    @Element( required = false )
    public Long entitiesPerCommit;

    @Element( required = false )
    public Long maxImportDuration;

    /**
     * @return the entitiesPerCommit
     */
    public Long getEntitiesPerCommit()
    {
        return entitiesPerCommit;
    }

    /**
     * @param entitiesPerCommit the entitiesPerCommit to set
     */
    public void setEntitiesPerCommit( Long entitiesPerCommit )
    {
        this.entitiesPerCommit = entitiesPerCommit;
    }

    /**
     * @return the maxImportDuration
     */
    public Long getMaxImportDuration()
    {
        return maxImportDuration;
    }

    /**
     * @param maxImportDuration the maxImportDuration to set
     */
    public void setMaxImportDuration( Long maxImportDuration )
    {
        this.maxImportDuration = maxImportDuration;
    }
}
