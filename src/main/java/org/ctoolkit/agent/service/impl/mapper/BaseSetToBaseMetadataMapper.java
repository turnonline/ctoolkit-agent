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
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ISet;
import org.ctoolkit.agent.model.ISetItem;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * Mapper for frontend to backend metadata model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class BaseSetToBaseMetadataMapper<F extends ISet<?>, B extends BaseMetadata<BI>, FI extends ISetItem, BI extends BaseMetadataItem<B>>
        extends CustomMapper<F, B>
{
    @Inject
    private Datastore datastore;

    @Override
    @SuppressWarnings( "unchecked" )
    public void mapAtoB( F set, B metadata, MappingContext context )
    {
        metadata.setName( set.getName() );

        extraMapAToB( set, metadata );

        // remove items in backend object not existed in set from frontend
        Iterator<BI> iterator = metadata.getItems().iterator();
        while ( iterator.hasNext() )
        {
            BI next = iterator.next();
            if ( !contains( set, next ) )
            {
                iterator.remove();
                datastore.delete( next.key() );
            }
        }

        for ( ISetItem anItem : set.getItems() )
        {
            BI item = metadata.getItemByIdOrCreateNewOne( anItem.getId() );
            if ( anItem.getData() != null )
            {
                item.setData( anItem.getData() );
            }
            item.setDataType( anItem.getDataType() );
            item.setName( anItem.getName() );

            extraMapAItemToBItem( ( FI ) anItem, item );
        }
    }

    @Override
    public void mapBtoA( B metadata, F set, MappingContext context )
    {
        set.setId( metadata.getId() );
        set.setName( metadata.getName() );
        set.setJobId( metadata.getJobId() );
        set.setCreateDate( metadata.getCreateDate() );
        set.setUpdateDate( metadata.getUpdateDate() );

        extraMapBToA( metadata, set );

        for ( BI item : metadata.getItems() )
        {
            FI anItem = newItem();
            anItem.setId( item.getId() );
            anItem.setName( item.getName() );
            anItem.setCreateDate( item.getCreateDate() );
            anItem.setUpdateDate( item.getUpdateDate() );
            anItem.setDataType( item.getDataType() );
            anItem.setDataLength( item.getDataLength() );
            anItem.setFileName( item.getFileName() );
            anItem.setState( item.getState() );
            anItem.setError( item.getError() );

            extraMapBItemToAItem( item, anItem );

            addItem( set, anItem );
        }
    }

    protected void extraMapAToB( F set, B metadata )
    {
    }

    protected void extraMapBToA( B metadata, F set )
    {
    }

    protected void extraMapAItemToBItem( FI anItem, BI item )
    {
    }

    protected void extraMapBItemToAItem( BI item, FI anItem )
    {
    }

    protected abstract FI newItem();

    protected abstract void addItem( F anImport, FI anItem );

    private boolean contains( ISet<?> set, BI beItem )
    {
        for ( ISetItem item : set.getItems() )
        {
            if ( beItem.getId().equals( item.getId() ) )
            {
                return true;
            }
        }

        return false;
    }
}
