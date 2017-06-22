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

package org.ctoolkit.agent.service.impl.mapper;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;

import javax.inject.Inject;

/**
 * Entity builder factory used to create Entity.Builder
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class EntityBuilderFactory
        implements ObjectFactory<Entity.Builder>
{
    private final Datastore datastore;

    private final EntityDecoder decoder;

    private final String projectId;

    @Inject
    public EntityBuilderFactory( Datastore datastore,
                                 EntityDecoder decoder,
                                 @ProjectId String projectId )
    {
        this.datastore = datastore;
        this.decoder = decoder;
        this.projectId = projectId;
    }

    @Override
    public Entity.Builder create( Object o, MappingContext mappingContext )
    {
        ChangeSetEntity changeSetEntity = ( ChangeSetEntity ) o;

        // generate parent key
        Key parentKey = null;

        // parent key has top priority
        if ( changeSetEntity.getParentKey() != null )
        {
            parentKey = decoder.parseKeyByIdOrName( changeSetEntity.getParentKey() );
        }
        // parent kind/id
        else if ( changeSetEntity.getParentId() != null && changeSetEntity.getParentKind() != null )
        {
            parentKey = Key.newBuilder( projectId, changeSetEntity.getParentKind(), changeSetEntity.getParentId() ).build();
        }
        // parent kind/name has the lowest priority in the reference chain
        else if ( changeSetEntity.getParentName() != null && changeSetEntity.getParentKind() != null )
        {
            parentKey = Key.newBuilder( projectId, changeSetEntity.getParentKind(), changeSetEntity.getParentName() ).build();
        }

        // generate the entity
        Entity.Builder entityBuilder;

        // look for parent kind/id
        if ( changeSetEntity.getId() != null )
        {

            if ( parentKey != null )
            {
                Key key = Key
                        .newBuilder( parentKey, changeSetEntity.getKind(), changeSetEntity.getId() )
                        .build();

                entityBuilder = Entity.newBuilder( key );
            }
            else
            {
                Key key = Key
                        .newBuilder( projectId, changeSetEntity.getKind(), changeSetEntity.getId() )
                        .build();

                entityBuilder = Entity.newBuilder( key );
            }
        }
        // look for parent kind/name
        else if ( changeSetEntity.getName() != null )
        {

            if ( parentKey != null )
            {
                Key key = Key
                        .newBuilder( parentKey, changeSetEntity.getKind(), changeSetEntity.getName() )
                        .build();

                entityBuilder = Entity.newBuilder( key );
            }
            else
            {
                Key key = Key
                        .newBuilder( projectId, changeSetEntity.getKind(), changeSetEntity.getName() )
                        .build();

                entityBuilder = Entity.newBuilder( key );
            }
        }
        // let the datastore allocate id
        else
        {
            Key key = datastore
                    .allocateId( Key
                            .newBuilder( projectId, changeSetEntity.getKind() )
                            .build()
                    );

            entityBuilder = Entity.newBuilder( key );
        }

        return entityBuilder;
    }
}
