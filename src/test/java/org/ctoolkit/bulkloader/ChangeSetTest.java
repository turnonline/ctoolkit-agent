package org.ctoolkit.bulkloader;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionModule;
import org.ctoolkit.bulkloader.changesets.ChangeSetVersionService;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntityProperty;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;
import org.ctoolkit.bulkloader.changesets.model.ChangeSets;
import org.ctoolkit.bulkloader.changesets.model.KindOp;
import org.ctoolkit.bulkloader.changesets.model.KindPropOp;
import org.ctoolkit.bulkloader.common.BulkLoader;
import org.ctoolkit.bulkloader.common.BulkLoaderException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class ChangeSetTest
{

    private static LocalServiceTestHelper helper = null;

    @BeforeClass
    public static void setUp() throws Exception
    {
        LocalDatastoreServiceTestConfig tc = new LocalDatastoreServiceTestConfig();
        /*
            tc.setNoStorage(false);
           tc.setBackingStoreLocation("c:\\temp\\local_db.bin");
            tc.setNoStorage(false);
            tc.setNoIndexAutoGen(false);
        helper = new LocalServiceTestHelper(tc);
//    	helper.setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvAppId("comvai-testing");
        helper.setEnvAppId("comvai-testing");
        helper.setUp();
        */
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        // helper.tearDown();
    }

    @Test
    public void testChangeSets() throws BulkLoaderException
    {
        // testChangeSetScripts();
        // testUserFileChangeSetIterator();
        // testChangeSetHistory();
        // testExport();
        // testImport();
        // testUpdate();
    }

    public void testUpdate() throws BulkLoaderException
    {
        BulkLoader bl = Guice.createInjector( new BulkLoaderModule() ).getInstance( BulkLoader.class );
        Properties props = new Properties();
        //bl.doUpdate(props);
    }

    public void testImport() throws BulkLoaderException
    {
        BulkLoader bl = Guice.createInjector( new BulkLoaderModule() ).getInstance( BulkLoader.class );
        Properties props = new Properties();
        // bl.doExport();
    }

    public void testExport() throws BulkLoaderException
    {
        BulkLoader bl = Guice.createInjector( new BulkLoaderModule() ).getInstance( BulkLoader.class );
        Properties props = new Properties();
        // bl.doExport();
    }

    public void testChangeSetHistory()
    {
        // try to reload the changeset
        File inputsource = new File( "changesets.xml" );
        ChangeSets cs = null;
        try
        {
            cs = new Persister().read( ChangeSets.class, inputsource );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        ChangeSetVersionService csvs = Guice.createInjector( new ChangeSetVersionModule() ).getInstance( ChangeSetVersionService.class );
        ChangeSetVersion hist = csvs.getCurrentChangeSetVersion();
        System.out.println( hist );
    }

    public void testChangeSetScripts()
    {
        ChangeSets changesets = new ChangeSets();
        changesets.setChangeSets( new ArrayList<ChangeSet>() );

        changesets.getChangeSets().add( generateRandomChangeSet( ( long ) 1 ) );
        changesets.getChangeSets().add( generateRandomChangeSet( ( long ) 2 ) );
        changesets.getChangeSets().add( generateRandomChangeSet( ( long ) 3 ) );

        File source = new File( "changesets.xml" );
        try
        {
            new Persister().write( changesets, source );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        // try to reload the changeset
        File inputsource = new File( "changesets.xml" );
        ChangeSets readChSet = null;
        try
        {
            readChSet = new Persister().read( ChangeSets.class, inputsource );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private ChangeSet generateRandomChangeSet( Long version )
    {
        ChangeSet cs = ChangeSet.createChangeSet( version, "Atok" + version );

        KindOp removeKind = new KindOp( "remove", "kind", "reason1" );
        KindOp cleanKind = new KindOp( "clean", "kind", null );
        KindPropOp removeProp = new KindPropOp( "remove", "kind", "prop1", ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN, null, null, null, null, "len tak" );
        KindPropOp addProp = new KindPropOp( "add", "kind", "prop2", ChangeSetEntityProperty.PROPERTY_TYPE_DATE, null, null, null, null, "len tak" );
        KindPropOp updateProp = new KindPropOp( "update", "kind", "prop3", ChangeSetEntityProperty.PROPERTY_TYPE_SHORTBLOB, "newname", "newtype", "true", null, "len tak" );

        cs.getModel().getKindOps().add( removeKind );
        cs.getModel().getKindOps().add( cleanKind );

        cs.getModel().getKindPropOps().add( addProp );
        cs.getModel().getKindPropOps().add( removeProp );
        cs.getModel().getKindPropOps().add( updateProp );

        ChangeSetEntityProperty prop1 = new ChangeSetEntityProperty( "prop1", "type1", "value1" );
        ChangeSetEntityProperty prop2 = new ChangeSetEntityProperty( "prop2", "type2", "value2" );
        ChangeSetEntityProperty prop3 = new ChangeSetEntityProperty( "prop3", "type3", "value3" );

        ChangeSetEntity ent1 = new ChangeSetEntity( "Member", "666", "delete" );
        ChangeSetEntity ent2 = new ChangeSetEntity( "Member", "666", null );

        ent2.addProperty( prop1 );
        ent2.addProperty( prop2 );
        ent2.addProperty( prop3 );

        cs.addEntity( ent1 );
        cs.addEntity( ent2 );

        return cs;
    }
}
