package org.ctoolkit.bulkloader.conf;

import org.ctoolkit.bulkloader.conf.model.BulkLoaderConfig;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.conf.model.ExportJobs;
import org.ctoolkit.bulkloader.conf.model.ImportConfig;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.logging.Logger;

/**
 * Bulk loader configuration module
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderConfiguration
        extends BaseConfiguration
{

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger( BulkLoaderConfiguration.class.getName() );

    private static final String MAIN_CONFIG_FILE = "/dataset/bulkloader.xml";

    private static final String EXPORT_JOBS_FILE = "/dataset/export_jobs.xml";

    private static final String IMPORT_CONFIG_FILE = "/dataset/import_config.xml";

    private ExportJobs exportJobs;

    private ImportConfig importConfig;

    private BulkLoaderConfig bulkLoaderConfig;

    @Override
    public ExportJobs getExportJobs()
    {
        if ( null == exportJobs )
        {
            try
            {
                logger.info( "Loading export jobs from " + EXPORT_JOBS_FILE );
                exportJobs = new Persister().read( ExportJobs.class, getClass().getResourceAsStream( EXPORT_JOBS_FILE ) );
            }
            catch ( Exception e )
            {
                logger.severe( "Exception by reading export config file " + EXPORT_JOBS_FILE );
            }
        }
        return exportJobs;
    }

    @Override
    public ExportJob getExportJob( String exportJob )
    {
        if ( null == exportJobs )
        {
            getExportJobs();
        }
        if ( null != exportJobs )
        {
            for ( ExportJob eJob : exportJobs.getJobs() )
            {
                if ( exportJob.equals( eJob.getName() ) )
                {
                    return eJob;
                }
            }
        }
        return null;
    }

    @Override
    public ImportConfig getImportConfig()
    {
        if ( null == importConfig )
        {
            try
            {
                importConfig = new Persister().read( ImportConfig.class, getClass().getResourceAsStream( IMPORT_CONFIG_FILE ) );
            }
            catch ( Exception e )
            {
                logger.severe( "Exception by reading import config file " + IMPORT_CONFIG_FILE );
            }
        }
        return importConfig;
    }

    @Override
    public BulkLoaderConfig getBulkLoaderConfig()
    {
        if ( null == bulkLoaderConfig )
        {
            try
            {
                bulkLoaderConfig = new Persister().read( BulkLoaderConfig.class, getClass().getResourceAsStream( MAIN_CONFIG_FILE ) );
            }
            catch ( Exception e )
            {
                logger.severe( "Exception by reading import config file " + MAIN_CONFIG_FILE );
            }
        }
        return bulkLoaderConfig;
    }

    public void writeExportJobs( final ExportJobs config )
    {
        if ( null == config )
        {
            return;
        }

        try
        {
            new Persister().write( config, new File( BulkLoaderConfiguration.EXPORT_JOBS_FILE ) );
        }
        catch ( Exception e )
        {
            logger.severe( e.toString() );
        }
    }

    public void writeImportConfig( final ImportConfig config )
    {
        if ( null == config )
        {
            return;
        }

        try
        {
            new Persister().write( config, new File( BulkLoaderConfiguration.IMPORT_CONFIG_FILE ) );
        }
        catch ( Exception e )
        {
            logger.severe( e.toString() );
        }
    }
}
