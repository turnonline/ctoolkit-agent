package org.ctoolkit.migration.agent.service.impl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Charsets;
import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.migration.agent.UseCaseEnvironment;
import org.ctoolkit.migration.agent.model.ChangeSet;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.util.XmlUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBeanIT
        extends UseCaseEnvironment
{
    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( UseCaseEnvironment.class );

    @Inject
    private ChangeSetService service;

    @Inject
    private DatastoreService datastoreService;

    private ChangeSet DROP_ENTITY;
    private ChangeSet CLEAN_ENTITY;
    private ChangeSet ADD_ENTITY;
    private ChangeSet REMOVE_ENTITY_PROPERTY;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_NAME;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE_NEW_VALUE;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_VALUE;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_TYPE;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_TYPE_NEW_VALUE;
    private ChangeSet CHANGE_ENTITY_PROPERTY__NEW_VALUE;

    @Before
    public void setUp()
    {
        // ignore white spaces when comparing XMLs
        XMLUnit.setIgnoreWhitespace( true );

        DROP_ENTITY = load( "change__drop_entity.xml" );
        CLEAN_ENTITY = load( "change__clean_entity.xml" );
        ADD_ENTITY = load( "import__add_entity.xml" );
        REMOVE_ENTITY_PROPERTY = load( "change__remove_entity_property.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_NAME = load( "change__entity_property_newName.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE = load( "change__entity_property_newNameNewType.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE_NEW_VALUE = load( "change__entity_property_newNameNewTypeNewValue.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_VALUE = load( "change__entity_property_newNameNewValue.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_TYPE = load( "change__entity_property_newType.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_TYPE_NEW_VALUE = load( "change__entity_property_newTypeNewValue.xml" );
        CHANGE_ENTITY_PROPERTY__NEW_VALUE = load( "change__entity_property_newValue.xml" );
    }

    @After
    public void tearDown()
    {
        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            datastoreService.delete( entity.getKey() );
        }
    }

    // -- importChangeSet

    @Test
    public void importChangeSet_DropEntity() throws Exception
    {
        createRecord();

        service.importChangeSet( DROP_ENTITY );

        assertFalse( datastoreService.prepare( new Query( "Country" ) ).asIterable().iterator().hasNext() );
    }

    @Test
    public void importChangeSet_CleanEntity() throws Exception
    {
        createRecord();

        service.importChangeSet( CLEAN_ENTITY );

        assertFalse( datastoreService.prepare( new Query( "Country" ) ).asIterable().iterator().hasNext() );
    }

    @Test
    public void importChangeSet_AddEntity() throws Exception
    {
        service.importChangeSet( ADD_ENTITY );

        assertTrue( datastoreService.prepare( new Query( "Country" ) ).asIterable().iterator().hasNext() );
    }

    @Test
    public void importChangeSet_RemoveEntityProperty() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( REMOVE_ENTITY_PROPERTY );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertFalse( entity.hasProperty( "label" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewName() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_NAME );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertFalse( entity.hasProperty( "label" ) );
            assertEquals( "English", entity.getProperty( "description" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewNameNewType() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertFalse( entity.hasProperty( "extId" ) );
            assertEquals( 10D, entity.getProperty( "externalId" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewNameNewTypeNewValue() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_TYPE_NEW_VALUE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertFalse( entity.hasProperty( "extId" ) );
            assertEquals( 20D, entity.getProperty( "externalId" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewNameNewValue() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_NAME_NEW_VALUE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertFalse( entity.hasProperty( "extId" ) );
            assertEquals( 20L, entity.getProperty( "externalId" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewType() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_TYPE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertEquals( 10D, entity.getProperty( "extId" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewTypeNewValue() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_TYPE_NEW_VALUE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertEquals( 20D, entity.getProperty( "extId" ) );
        }
    }

    @Test
    public void importChangeSet_ChangeEntityProperty_NewValue() throws Exception
    {
        for ( int i = 1; i < 1000; i++ )
        {
            createRecord( i );
        }

        service.importChangeSet( CHANGE_ENTITY_PROPERTY__NEW_VALUE );

        for ( Entity entity : datastoreService.prepare( new Query( "Country" ) ).asIterable() )
        {
            assertEquals( 20L, entity.getProperty( "extId" ) );
        }
    }

    // -- exportChangeSet

    @Test
    public void exportChangeSet() throws Exception
    {
        createRecord();

        byte[] export = service.exportChangeSet( "Country" );

        InputStream actual = new ByteArrayInputStream( export );
        InputStream expected = ChangeSetServiceBeanIT.class.getResourceAsStream( "/export/country.xml" );

        System.out.println(new String(export, Charsets.UTF_8));

        XMLAssert.assertXMLEqual( new InputSource( expected ), new InputSource( actual ) );
    }

    // -- private helpers

    private void createRecord()
    {
        createRecord( 1 );
    }

    private void createRecord( int id )
    {
        Entity entity = new Entity( "Country", id );
        entity.setProperty( "code", "EN" );
        entity.setProperty( "label", "English" );
        entity.setProperty( "extId", 10 );
        datastoreService.put( entity );
    }

    private ChangeSet load( String xml )
    {
        return XmlUtils.unmarshall( ChangeSetServiceBeanIT.class.getResourceAsStream( "/dataset/" + xml ), ChangeSet.class );
    }
}