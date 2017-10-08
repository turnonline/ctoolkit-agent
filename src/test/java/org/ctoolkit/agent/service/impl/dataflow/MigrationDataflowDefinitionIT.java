package org.ctoolkit.agent.service.impl.dataflow;

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.JobMetrics;
import com.google.api.services.dataflow.model.MetricUpdate;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.common.base.Charsets;
import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.agent.UseCaseEnvironment;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.ISetItem;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.resource.MigrationSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperationAdd;
import org.ctoolkit.agent.resource.MigrationSetKindOperationChange;
import org.ctoolkit.agent.resource.MigrationSetKindOperations;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.util.XmlUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrationDataflowDefinitionIT
        extends UseCaseEnvironment
{
    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( UseCaseEnvironment.class );

    @Inject
    private EntityPool pool;

    @Inject
    @ProjectId
    private String projectId;

    private MigrationDataflowDefinition dataflow;

    @Inject
    protected Dataflow dataflowApi;

    @Before
    public void setUp() throws Exception
    {
        // mockEntities();
        MigrationMetadata migrationMetadata = mockMigrationMetadata();

        dataflow = new MigrationDataflowDefinition( migrationMetadata.getId() );
    }

    @Test
    public void testMockEntities() throws Exception
    {
        for ( int i = 1; i <= 10000; i++ )
        {
            Key key = Key.newBuilder( projectId, "City", i ).build();

            Entity city = Entity.newBuilder( key )
                    .set( "name", "New York" )
                    .build();
            pool.put( city );

            if ( i % 500 == 0 )
            {
                System.out.println( "Created: " + i + " items" );
            }
        }

        pool.flush();
    }

    @Test
    public void testDeleteEntities() throws Exception
    {
        for ( int i = 1; i <= 1000; i++ )
        {
            pool.delete( Key.newBuilder( projectId, "City", i ).build() );

            if ( i % 100 == 0 )
            {
                System.out.println( "Deleted: " + i + " items" );
            }
        }

        pool.flush();
    }

    @Test
    public void run() throws Exception
    {
        dataflow.run();
    }

    @Test
    public void getStatus() throws Exception
    {
        while ( true )
        {
            String jobId = "2017-10-06_13_05_44-16697639906962320284";

            JobMetrics jobMetrics = dataflowApi.projects().jobs().getMetrics( projectId, jobId ).execute();
            for ( MetricUpdate update : jobMetrics.getMetrics() )
            {
                if ( update.getName().getContext().get( "original_name" ).equals( "Read entities from split queries-out0-ElementCount" ))
                {
                    System.out.println( "Processed items: " + update.getScalar() );
                    break;
                }
            }

            Thread.sleep( 5000 );
        }
    }

    private MigrationMetadata mockMigrationMetadata()
    {
        MigrationMetadata migrationMetadata = new MigrationMetadata();
        migrationMetadata.setName( "Test" );

//        MigrationMetadataItem item1 = new MigrationMetadataItem( migrationMetadata );
//        item1.setDataType( ISetItem.DataType.XML );
//        item1.setData( createBlob1() );
//        item1.setName( "TestItem1" );
//        item1.setFileName( "blob1" );
//        migrationMetadata.getItems().add( item1 );

        MigrationMetadataItem item2 = new MigrationMetadataItem( migrationMetadata );
        item2.setDataType( ISetItem.DataType.XML );
        item2.setData( createBlob2() );
        item2.setName( "TestItem2" );
        item2.setFileName( "blob2" );
        migrationMetadata.getItems().add( item2 );

        migrationMetadata.save();

        return migrationMetadata;
    }

    private byte[] createBlob1()
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

    private byte[] createBlob2()
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
}