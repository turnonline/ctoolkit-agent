package org.ctoolkit.agent.service.impl;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.common.base.Charsets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import mockit.Mock;
import mockit.MockUp;
import org.ctoolkit.agent.resource.MigrationSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperationAdd;
import org.ctoolkit.agent.resource.MigrationSetKindOperationChange;
import org.ctoolkit.agent.resource.MigrationSetKindOperations;
import org.ctoolkit.agent.util.XmlUtils;

import javax.inject.Singleton;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class TestStorageModule
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
        // run local emulator by > gcloud beta emulators datastore start --project=ctoolkit-agent-morty
        return DatastoreOptions.getDefaultInstance().toBuilder()
//                .setHost( "http://localhost:8081" )
                .build()
                .getService();
    }

    @Provides
    @Singleton
    public Storage provideStorage()
    {
        return new MockUp<Storage>()
        {
            @Mock
            byte[] readAllBytes( BlobId blob, Storage.BlobSourceOption... options )
            {
                if ( blob.getName().equals( "blob1" ) )
                {
                    MigrationSet migrationSet = new MigrationSet();
                    migrationSet.setOperations( new MigrationSetKindOperations() );

                    MigrationSetKindOperationAdd add = new MigrationSetKindOperationAdd();
                    migrationSet.getOperations().getAdd().add( add );
                    add.setKind( "Person" );
                    add.setProperty( "name" );

                    MigrationSetKindOperationChange change = new MigrationSetKindOperationChange();
                    migrationSet.getOperations().getChange().add( change );
                    change.setKind( "Person" );
                    change.setProperty( "name" );
                    change.setNewName( "_name" );

                    return XmlUtils.marshall( migrationSet ).getBytes( Charsets.UTF_8 );
                }
                else if ( blob.getName().equals( "blob2" ) )
                {
                    MigrationSet migrationSet = new MigrationSet();
                    migrationSet.setOperations( new MigrationSetKindOperations() );

                    MigrationSetKindOperationChange change = new MigrationSetKindOperationChange();
                    migrationSet.getOperations().getChange().add( change );
                    change.setKind( "City" );
                    change.setProperty( "name" );
                    change.setNewName( "fullName" );

                    return XmlUtils.marshall( migrationSet ).getBytes( Charsets.UTF_8 );
                }

                throw new IllegalArgumentException( "Blob mock does not exists for name: " + blob.getName() );
            }
        }.getMockInstance();
    }
}
