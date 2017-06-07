package org.ctoolkit.agent.service.impl.datastore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.BaseEntity;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class KeyProvider
{
    @Inject
    @ProjectId
    private static String projectId;

    @Inject
    private static Datastore datastore;

    public Key key( BaseEntity baseEntity )
    {
        String kind = baseEntity.getClass().getAnnotation( EntityMarker.class ).name();
        Long id = baseEntity.getId();

        if ( id == null )
        {
            return datastore.allocateId( Key.newBuilder( projectId, kind ).build() );
        }

        return Key.newBuilder( projectId, kind, id ).build();
    }

    public Key key( BaseEntity baseEntity, BaseEntity parent )
    {
        PathElement parentPath = PathElement.of( parent.key().getKind(), parent.key().getId() );
        String kind = baseEntity.getClass().getAnnotation( EntityMarker.class ).name();
        Long id = baseEntity.getId();

        if ( id == null )
        {
            return datastore.allocateId( Key.newBuilder( projectId, kind ).addAncestor( parentPath ).build() );
        }
        else
        {
            return Key.newBuilder( projectId, kind, id ).addAncestor( parentPath ).build();
        }
    }
}
