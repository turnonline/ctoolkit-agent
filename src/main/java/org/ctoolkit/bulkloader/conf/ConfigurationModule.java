package org.ctoolkit.bulkloader.conf;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ConfigurationModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( Configuration.class ).to( BulkLoaderConfiguration.class ).in( Singleton.class );
    }
}
