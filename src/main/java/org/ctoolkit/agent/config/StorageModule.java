package org.ctoolkit.agent.config;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * Storage module
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class StorageModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
    }

    @Provides
    @Singleton
    public Datastore provideDatastore()
    {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Provides
    @Singleton
    public Storage provideStorage()
    {
        return StorageOptions.getDefaultInstance().getService();
    }
}
