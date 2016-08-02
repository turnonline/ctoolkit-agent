package org.ctoolkit.migration.service.impl.datastore.mapper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.migration.model.ChangeSetEntity;
import org.ctoolkit.migration.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityFactory
        implements ObjectFactory<Entity>
{
    private final EntityEncoder encoder;

    @Inject
    public EntityFactory( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public Entity create( Object o, MappingContext mappingContext )
    {
        ChangeSetEntity changeSetEntity = ( ChangeSetEntity ) o;

        Entity entity;

        // cannot be both id and name specified
        // TODO: validation: exception, or take id as higher priority?

        // generate parent key
        Key parentKey = null;

        // parentEntityId has top priority
        if ( changeSetEntity.getParentKey() != null )
        {
            parentKey = encoder.parseKeyByIdOrName( changeSetEntity.getParentKey() );
            // parent kind/id
        }
        else if ( changeSetEntity.getParentId() != null && changeSetEntity.getParentKind() != null )
        {
            parentKey = KeyFactory.createKey( changeSetEntity.getParentKind(), changeSetEntity.getParentId() );
            // parent kind/name has the lowest priority in the reference chain
        }
        else if ( changeSetEntity.getParentName() != null && changeSetEntity.getParentKind() != null )
        {
            parentKey = KeyFactory.createKey( changeSetEntity.getParentKind(), changeSetEntity.getParentName() );
        }

        // generate the entity

        // look for a key property
        if ( changeSetEntity.getKey() != null )
        {
            // ignore parent key, because it has to be composed within the entity key
            entity = new Entity( KeyFactory.stringToKey( changeSetEntity.getKey() ) );
        }
        else if ( changeSetEntity.getId() != null )
        {
            // check if there is id changeSetEntity
            // look for parent kind/id
            if ( parentKey != null )
            {
                // build the entity key
                Key key = new KeyFactory.Builder( parentKey ).addChild( changeSetEntity.getKind(), changeSetEntity.getId() ).getKey();
                entity = new Entity( key );
            }
            else
            {
                entity = new Entity( KeyFactory.createKey( changeSetEntity.getKind(), changeSetEntity.getId() ) );
            }
        }
        else if ( changeSetEntity.getName() != null )
        {
            // build the entity key
            if ( parentKey != null )
            {
                entity = new Entity( changeSetEntity.getKind(), changeSetEntity.getName(), parentKey );
            }
            else
            {
                entity = new Entity( changeSetEntity.getKind(), changeSetEntity.getName() );
            }
        }
        else
        {
            entity = new Entity( changeSetEntity.getKind() );
        }

        return entity;
    }
}
