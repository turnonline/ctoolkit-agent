/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.service.impl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Charsets;
import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.agent.UseCaseEnvironment;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.util.XmlUtils;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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

        ChangeSet export = service.exportChangeSet( "Country" );
        byte[] data = XmlUtils.marshall( export ).getBytes( Charsets.UTF_8 );

        InputStream actual = new ByteArrayInputStream( data );
        InputStream expected = ChangeSetServiceBeanIT.class.getResourceAsStream( "/export/country.xml" );

        System.out.println( new String( data, Charsets.UTF_8 ) );

        XMLAssert.assertXMLEqual( new InputSource( expected ), new InputSource( actual ) );
    }

    // -- kinds

    @Test
    public void testKinds() throws Exception
    {
        createRecord();

        Entity sysKind = new Entity( "_ExportMetadata", 1 );
        datastoreService.put( sysKind );

        List<KindMetaData> kinds = service.kinds();

        assertEquals( 1, kinds.size() );
        assertEquals( "Country", kinds.get( 0 ).getKind() );
        assertEquals( "", kinds.get( 0 ).getNamespace() );
    }

    // -- kinds

    @Test
    public void testProperties() throws Exception
    {
        createRecord();

        List<PropertyMetaData> properties = service.properties( "Country" );

        assertEquals( 3, properties.size() );

        assertEquals( "code", properties.get( 0 ).getProperty() );
        assertEquals( "string", properties.get( 0 ).getType() );
        assertEquals( "Country", properties.get( 0 ).getKind() );
        assertEquals( "", properties.get( 0 ).getNamespace() );

        assertEquals( "extId", properties.get( 1 ).getProperty() );
        assertEquals( "int64", properties.get( 1 ).getType() );
        assertEquals( "Country", properties.get( 1 ).getKind() );
        assertEquals( "", properties.get( 1 ).getNamespace() );

        assertEquals( "label", properties.get( 2 ).getProperty() );
        assertEquals( "string", properties.get( 2 ).getType() );
        assertEquals( "Country", properties.get( 2 ).getKind() );
        assertEquals( "", properties.get( 2 ).getNamespace() );
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