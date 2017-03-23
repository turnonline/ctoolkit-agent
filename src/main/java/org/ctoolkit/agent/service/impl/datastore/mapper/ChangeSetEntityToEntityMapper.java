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

package org.ctoolkit.agent.service.impl.datastore.mapper;

import com.google.appengine.api.datastore.Entity;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

/**
 * Mapper for {@link ChangeSetEntity} to {@link Entity} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeSetEntityToEntityMapper
        extends CustomMapper<ChangeSetEntity, Entity>
{
    private final EntityEncoder encoder;

    private Logger logger = LoggerFactory.getLogger( ChangeSetEntityToEntityMapper.class );

    @Inject
    public ChangeSetEntityToEntityMapper( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public void mapAtoB( ChangeSetEntity changeSetEntity, Entity entity, MappingContext context )
    {
        // the kind has to be specified
        if ( changeSetEntity.getKind() == null )
        {
            logger.error( "Missing entity kind! It has to be specified" );
            return;
        }

        // changeSetEntity up the properties
        if ( changeSetEntity.hasProperties() )
        {
            for ( ChangeSetEntityProperty prop : changeSetEntity.getProperty() )
            {
                if ( null == prop.getValue() )
                {
                    entity.setProperty( prop.getName(), encoder.decodeProperty( prop.getType(), null ) );
                }
                else
                {
                    entity.setProperty( prop.getName(), encoder.decodeProperty( prop.getType(), prop.getValue() ) );
                }
            }
        }
    }

    @Override
    public void mapBtoA( Entity entity, ChangeSetEntity changeSetEntity, MappingContext context )
    {
        // create main entity
        changeSetEntity.setKind( entity.getKind() );
        changeSetEntity.setName( entity.getKey().getName() );
        if ( entity.getKey().getName() == null )
        {
            changeSetEntity.setId( entity.getKey().getId() );
        }

        changeSetEntity.setParentKey( encoder.formatKey( entity.getKey().getParent() ) );

        // changeSetEntity up entity properties
        for ( Map.Entry<String, Object> pairs : entity.getProperties().entrySet() )
        {
            ChangeSetEntityProperty property = encoder.encodeProperty( pairs.getKey(), pairs.getValue() );
            changeSetEntity.getProperty().add( property );
        }
    }
}