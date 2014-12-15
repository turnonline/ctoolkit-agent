package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root( name = "export-jobs" )
public class ExportJobs
{

    @ElementList( inline = true )
    private List<ExportJob> jobs;

    @Element
    private ExportConfig config;

    public ExportJobs()
    {
    }

    /**
     * @return the jobs
     */
    public List<ExportJob> getJobs()
    {
        return jobs;
    }

    /**
     * @param jobs the jobs to set
     */
    public void setJobs( List<ExportJob> jobs )
    {
        this.jobs = jobs;
    }

    /**
     * @return the config
     */
    public ExportConfig getConfig()
    {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig( ExportConfig config )
    {
        this.config = config;
    }
}
