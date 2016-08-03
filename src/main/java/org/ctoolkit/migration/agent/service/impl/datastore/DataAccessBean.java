package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.migration.agent.model.ChangeSet;
import org.ctoolkit.migration.agent.model.ChangeSetEntities;
import org.ctoolkit.migration.agent.model.ChangeSetEntity;
import org.ctoolkit.migration.agent.service.DataAccess;
import org.ctoolkit.migration.agent.service.impl.datastore.rule.ChangeRuleEngine;
import org.ctoolkit.migration.agent.service.impl.datastore.rule.IChangeRule;

import javax.inject.Inject;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * GAE datastore implementation of {@link DataAccess}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
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

    private final EntityPool pool;

    private final MapperFacade mapper;

    private final ChangeRuleEngine changeRuleEngine;

    @Inject
    protected DataAccessBean( DatastoreService datastore,
                              EntityPool pool,
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
        pool.put( entity );
    }

    @Override
    public ChangeSet exportChangeSet( String entityName )
    {
        ChangeSet changeSet = new ChangeSet();
        changeSet.setComment( "Export for entity " + entityName );
        changeSet.setAuthor( "ctoolkit-migration" );
        changeSet.setEntities( new ChangeSetEntities() );

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
                    pool.delete( entity.getKey() );
                }
            }
            else
            {
                break;
            }
        }

        pool.flush();
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

                    pool.put( entity );
                }

                offset += DEFAULT_COUNT_LIMIT;
            }
            else
            {
                break;
            }
        }

        pool.flush();
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
                    pool.put( entity );
                }

                offset += DEFAULT_COUNT_LIMIT;
            }
            else
            {
                break;
            }
        }

        pool.flush();
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
    public <T> void delete( Class<T> entity, String key )
    {
        ofy().delete().type( entity ).id( key ).now();
    }
}
