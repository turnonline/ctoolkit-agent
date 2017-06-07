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

package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.googlecode.objectify.Key;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.resource.ChangeSetEntities;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.datastore.rule.ChangeRuleEngine;
import org.ctoolkit.agent.service.impl.datastore.rule.IChangeRule;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * GAE datastore implementation of {@link DataAccess}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class DataAccessBean
        implements DataAccess
{
    /**
     * The default number of entities to put.delete from the data store
     */
    // TODO: configurable
    private static final int DEFAULT_COUNT_LIMIT = 100;

    @Deprecated
    private final DatastoreService datastoreService;

    private final Datastore datastore;

    private final Provider<EntityPool> pool;

    private final MapperFacade mapper;

    private final ChangeRuleEngine changeRuleEngine;

    @Inject
    protected DataAccessBean( DatastoreService datastoreService,
                              Datastore datastore,
                              Provider<EntityPool> pool,
                              MapperFacade mapper,
                              ChangeRuleEngine changeRuleEngine )
    {
        this.datastoreService = datastoreService;
        this.datastore = datastore;
        this.pool = pool;
        this.mapper = mapper;
        this.changeRuleEngine = changeRuleEngine;
    }

    @Override
    public void addEntity( ChangeSetEntity csEntity )
    {
        Entity entity = mapper.map( csEntity, Entity.class );
        pool.get().put( entity );
    }

    @Override
    public ChangeSet exportChangeSet( String entityName )
    {
        ChangeSet changeSet = new ChangeSet();
        changeSet.setComment( "Export for entity " + entityName );
        changeSet.setAuthor( "ctoolkit-agent" );
        changeSet.setEntities( new ChangeSetEntities() );

        // add entities
        Query query = new Query( entityName );
        PreparedQuery preparedQuery = datastoreService.prepare( query );
        for ( Entity entity : preparedQuery.asIterable() )
        {
            ChangeSetEntity changeSetEntity = mapper.map( entity, ChangeSetEntity.class );
            changeSet.getEntities().getEntity().add( changeSetEntity );
        }

        return changeSet;
    }

    @Override
    public void clearEntity( String kind )
    {
        dropEntity( kind );
    }

    @Override
    public void dropEntity( String kind )
    {
        while ( true )
        {
            Query query = new Query( kind ).setKeysOnly();
            PreparedQuery preparedQuery = datastoreService.prepare( query );
            List<Entity> entList = preparedQuery.asList( withLimit( DEFAULT_COUNT_LIMIT ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    pool.get().delete( entity.getKey() );
                }

                if ( entList.size() < DEFAULT_COUNT_LIMIT )
                {
                    pool.get().flush();
                }
            }
            else
            {
                break;
            }
        }

        pool.get().flush();
    }

    @Override
    public void addEntityProperty( String kind, String property, String newType, String newVal )
    {
        changeEntityProperty( kind, property, null, newType, newVal );
    }

    @Override
    public void changeEntityProperty( String kind, String property, String newName, String newType, String newVal )
    {
        int offset = 0;

        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery prepQuery = datastoreService.prepare( query );
            FetchOptions fetchOptions = withLimit( DEFAULT_COUNT_LIMIT );
            fetchOptions.offset( offset );

            List<Entity> entList = prepQuery.asList( fetchOptions );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    // property exists - change property
                    IChangeRule changeRule = changeRuleEngine.provideRule( newName, newType, newVal );
                    String name = changeRule.getName( property, newName );
                    Object value = changeRule.getValue( entity.getProperty( property ), newType, newVal );

                    // remove old property if exists
                    if ( entity.getProperties().containsKey( property ) )
                    {
                        entity.removeProperty( property );
                    }

                    // create new migrated property
                    entity.setProperty( name, value );

                    pool.get().put( entity );
                }

                offset += DEFAULT_COUNT_LIMIT;
            }
            else
            {
                break;
            }
        }

        pool.get().flush();
    }

    @Override
    public void removeEntityProperty( String kind, String property )
    {
        int offset = 0;

        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery pq = datastoreService.prepare( query );
            FetchOptions fetchOptions = withLimit( DEFAULT_COUNT_LIMIT );
            fetchOptions.offset( offset );

            List<Entity> entList = pq.asQueryResultList( fetchOptions );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    entity.removeProperty( property );
                    pool.get().put( entity );
                }

                offset += DEFAULT_COUNT_LIMIT;
            }
            else
            {
                break;
            }
        }

        pool.get().flush();
    }

    @Override
    public <T> T create( T entity )
    {
        ofy().save().entity( entity ).now();
        return entity;
    }

    @Override
    public <T> T update( T entity )
    {
        return create( entity );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T> T find( Class<T> entity, String key )
    {
        return ( T ) ofy().load().key( Key.create( key ) ).now();
    }

    @Override
    public <T extends BaseMetadata> List<T> find( BaseMetadataFilter<T> filter )
    {
        com.google.cloud.datastore.Query<com.google.cloud.datastore.Entity> query = com.google.cloud.datastore.Query.newEntityQueryBuilder()
                .setKind( filter.getMetadataClass().getAnnotation( EntityMarker.class ).name() )
                .setLimit( filter.getLength() )
                .setOffset( filter.getStart() )
                .addOrderBy( filter.isAscending() ? StructuredQuery.OrderBy.asc( filter.getOrderBy() ) : StructuredQuery.OrderBy.desc( filter.getOrderBy() ) )
                .build();

        List<T> list = new ArrayList<>();
        QueryResults<com.google.cloud.datastore.Entity> results = datastore.run( query );
        while ( results.hasNext() )
        {
            com.google.cloud.datastore.Entity entity = results.next();
            T metadata = (T) BaseMetadata.convert( entity, filter.getMetadataClass() );
            list.add( metadata );
        }

        return list;
    }

    @Override
    public List<MetadataAudit> find( AuditFilter filter )
    {
        com.googlecode.objectify.cmd.Query<MetadataAudit> query = ofy().load().type( MetadataAudit.class )
                .limit( filter.getLength() )
                .offset( filter.getStart() );

        if ( filter.getOrderBy() != null )
        {
            query = filter.isAscending() ? query.order( filter.getOrderBy() ) : query.order( "-" + filter.getOrderBy() );
        }

        if ( filter.getOwnerId() != null )
        {
            query = query.filter( "ownerId =", filter.getOwnerId() );
        }

        return query.list();
    }

    @Override
    public <T> void delete( Class<T> entity, String key )
    {
        ofy().delete().key( Key.create( key ) ).now();
    }

    @Override
    public List<KindMetaData> kinds()
    {
        List<KindMetaData> kinds = new ArrayList<>();
        Query q = new Query( Entities.KIND_METADATA_KIND );

        for ( Entity e : datastoreService.prepare( q ).asIterable() )
        {
            KindMetaData kind = new KindMetaData();
            kind.setKind( e.getKey().getName() );
            kind.setNamespace( e.getKey().getNamespace() );
            kinds.add( kind );
        }

        return kinds;
    }

    @Override
    public List<PropertyMetaData> properties( String kind )
    {
        ArrayList<PropertyMetaData> properties = new ArrayList<>();
        Query q = new Query( Entities.PROPERTY_METADATA_KIND );
        q.setAncestor( Entities.createKindKey( kind ) );


        //Build list of query results
        for ( Entity e : datastoreService.prepare( q ).asIterable() )
        {
            PropertyMetaData property = new PropertyMetaData();
            property.setProperty( e.getKey().getName() );
            property.setType( ( ( List ) e.getProperty( "property_representation" ) ).get( 0 ).toString().toLowerCase() );
            property.setKind( e.getParent().getName() );
            property.setNamespace( e.getKey().getNamespace() );
            properties.add( property );
        }

        return properties;
    }

    @Override
    public void flushPool()
    {
        pool.get().flush();
    }
}
