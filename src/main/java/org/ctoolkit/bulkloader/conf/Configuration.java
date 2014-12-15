package org.ctoolkit.bulkloader.conf;

import org.ctoolkit.bulkloader.conf.model.BulkLoaderConfig;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.conf.model.ExportJobs;
import org.ctoolkit.bulkloader.conf.model.ImportConfig;

import java.util.Properties;

/**
 * Configuration
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface Configuration
{

    public ExportJobs getExportJobs();

    public ExportJob getExportJob( final String exportJob );

    public ImportConfig getImportConfig();

    public BulkLoaderConfig getBulkLoaderConfig();

    public void addProperties( Properties props );

    public void setProperty( final String prop, final Object value );

    public Object getProperty( final String prop );
}
