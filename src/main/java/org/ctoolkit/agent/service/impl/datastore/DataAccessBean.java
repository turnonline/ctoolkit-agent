package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.model.ChangeSetEntity;
import org.ctoolkit.agent.service.DataAccess;

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

    private final EntityEncoder encoder;

    private final MapperFacade mapper;

    @Inject
    protected DataAccessBean( DatastoreService datastore,
                              EntityPool pool,
                              EntityEncoder encoder,
                              MapperFacade mapper )
    {
        this.datastore = datastore;
        this.pool = pool;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public void addEntity( ChangeSetEntity csEntity )
    {
        Entity entity = mapper.map( csEntity, Entity.class );
        pool.put( entity );
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
                    // TODO: implement different use cases - find out suitable design pattern
                    // property exists - change property
                    if ( entity.getProperties().containsKey( property ) )
                    {
                        if ( newName != null || newType != null )
                        {
                            entity.setProperty( newName, encoder.decodeProperty( newType, newVal ) );
                        }
                        else
                        {
                            entity.setProperty( property, encoder.decodeProperty( newType, newVal ) );
                        }
                    }
                    // property does not exists - add new property
                    else
                    {
                        entity.setProperty( property, encoder.decodeProperty( newType, newVal ) );
                    }

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
        return (T) ofy().load().key( Key.create(key) ).now();
    }

    @Override
    public <T> void delete( Class<T> entity, String key )
    {
        ofy().delete().type( entity ).id( key ).now();
    }
}
