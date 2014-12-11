package org.ctoolkit.bulkloader;

import com.comvai.services.blob.BlobUploadCallback;
import com.google.inject.AbstractModule;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionModule;
import org.ctoolkit.bulkloader.common.BulkLoader;
import org.ctoolkit.bulkloader.common.BulkLoaderImpl;
import org.ctoolkit.bulkloader.conf.BulkLoaderConfiguration;
import org.ctoolkit.bulkloader.conf.Configuration;
import org.ctoolkit.bulkloader.dataprocessor.DataProcessorModule;

import javax.inject.Singleton;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        install( new DataProcessorModule() );
        install( new ChangeSetVersionModule() );

        bind( BulkLoader.class ).to( BulkLoaderImpl.class ).in( Singleton.class );
        bind( Configuration.class ).to( BulkLoaderConfiguration.class ).in( Singleton.class );
        bind( BlobUploadCallback.class ).to( EmptyBlobUploadCallback.class ).in( Singleton.class );

        // retired, see #70
        //bind( AuthService.class ).to( AuthServiceImpl.class ).in( Singleton.class );

        // bind Test and Prod Environment Namespace
        //bind( String.class ).annotatedWith( Names.named( PropertyService.TEST_ENV_NAMESPACE ) ).toInstance( "cshop-assembly" );
        //bind( String.class ).annotatedWith( Names.named( PropertyService.PRODUCTION_ENV_NAMESPACE ) ).toInstance( "c-shop" );

        requestStaticInjection( ImportTask.class );
        requestStaticInjection( ExportTask.class );
        requestStaticInjection( UpgradeTask.class );
    }
}
