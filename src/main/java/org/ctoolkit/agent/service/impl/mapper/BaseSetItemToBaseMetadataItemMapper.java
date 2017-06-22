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

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ISetItem;

/**
 * Mapper for frontend to backend metadata item model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class BaseSetItemToBaseMetadataItemMapper<FI extends ISetItem, BI extends BaseMetadataItem>
        extends CustomMapper<FI, BI>
{
    @Override
    @SuppressWarnings( "unchecked" )
    public void mapAtoB( FI setItem, BI metadataItem, MappingContext context )
    {
        metadataItem.setName( setItem.getName() );
        metadataItem.setData( setItem.getData() );
        metadataItem.setDataType( setItem.getDataType() );
    }

    @Override
    public void mapBtoA( BI metadataItem, FI setItem, MappingContext context )
    {
        setItem.setId( metadataItem.getId() );
        setItem.setName( metadataItem.getName() );
        setItem.setCreateDate( metadataItem.getCreateDate() );
        setItem.setUpdateDate( metadataItem.getUpdateDate() );
        setItem.setData( metadataItem.getData() );
        setItem.setFileName( metadataItem.getFileName() );
        setItem.setState( metadataItem.getState() );
        setItem.setDataType( metadataItem.getDataType() );
        setItem.setDataLength( metadataItem.getDataLength() );
        setItem.setError( metadataItem.getError() );
    }
}
