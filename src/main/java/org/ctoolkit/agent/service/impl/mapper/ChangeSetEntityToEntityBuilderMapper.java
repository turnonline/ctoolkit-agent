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

import com.google.cloud.datastore.Entity;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Mapper for {@link ChangeSetEntity} to {@link Entity} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeSetEntityToEntityBuilderMapper
        extends CustomMapper<ChangeSetEntity, Entity.Builder>
{
    private static Logger logger = LoggerFactory.getLogger( ChangeSetEntityToEntityBuilderMapper.class );

    private final EntityEncoder encoder;

    private final EntityDecoder decoder;

    @Inject
    public ChangeSetEntityToEntityBuilderMapper( EntityEncoder encoder,
                                                 EntityDecoder decoder)
    {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public void mapAtoB( ChangeSetEntity changeSetEntity, Entity.Builder entityBuilder, MappingContext context )
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
                entityBuilder.set( prop.getName(), decoder.decode( prop.getType(), prop.getValue() ) );
            }
        }
    }

    @Override
    public void mapBtoA( Entity.Builder entity, ChangeSetEntity changeSetEntity, MappingContext context )
    {
        // TODO: implement
//        // create main entity
//        changeSetEntity.setKind( entity.getKey().getKind() );
//        changeSetEntity.setName( entity.getKey().getName() );
//        if ( entity.getKey().getName() == null )
//        {
//            changeSetEntity.setId( entity.getKey().getId() );
//        }
//
//        changeSetEntity.setParentKey( encoder.formatKey( entity.getKey().getParent() ) );
//
//        // changeSetEntity up entity properties
//        for ( Map.Entry<String, Object> pairs : entity.getProperties().entrySet() )
//        {
//            ChangeSetEntityProperty property = encoder.encodeProperty( pairs.getKey(), pairs.getValue() );
//            changeSetEntity.getProperty().add( property );
//        }
    }
}
