package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.datastore.rule.ChangeRuleEngine;
import org.ctoolkit.agent.service.impl.datastore.rule.IChangeRule;
import org.ctoolkit.agent.shared.resources.ChangeSet;
import org.ctoolkit.agent.shared.resources.ChangeSetEntities;
import org.ctoolkit.agent.shared.resources.ChangeSetEntity;
import org.ctoolkit.agent.shared.resources.ChangeSetModel;
import org.ctoolkit.agent.shared.resources.ChangeSetModelKindOp;

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

    private final DatastoreService datastore;

    private final Provider<EntityPool> pool;

    private final MapperFacade mapper;

    private final ChangeRuleEngine changeRuleEngine;

    @Inject
    protected DataAccessBean( DatastoreService datastore,
                              Provider<EntityPool> pool,
                              MapperFacade mapper,
                              ChangeRuleEngine changeRuleEngine )
    {
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

        // add default model clean action
        changeSet.setModel( new ChangeSetModel() );
        ChangeSetModelKindOp kinOp = new ChangeSetModelKindOp();
        kinOp.setKind( entityName );
        kinOp.setOp( ChangeSetModelKindOp.OP_CLEAN );
        changeSet.getModel().getKindOp().add( kinOp );

        // add entities
        Query query = new Query( entityName );
        PreparedQuery preparedQuery = datastore.prepare( query );
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
            PreparedQuery preparedQuery = datastore.prepare( query );
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
            PreparedQuery prepQuery = datastore.prepare( query );
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
            PreparedQuery pq = datastore.prepare( query );
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
        com.googlecode.objectify.cmd.Query<T> query = ofy().load().type( filter.getMetadataClass() )
                .limit( filter.getLength() )
                .offset( filter.getStart() );

        if ( filter.getOrderBy() != null )
        {
            query = filter.isAscending() ? query.order( filter.getOrderBy() ) : query.order( "-" + filter.getOrderBy() );
        }

        return query.list();
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

        for ( Entity e : datastore.prepare( q ).asIterable() )
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
        for ( Entity e : datastore.prepare( q ).asIterable() )
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
