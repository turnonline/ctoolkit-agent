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

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.resource.ChangeBatch;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeMetadataItemFactory
        implements ObjectFactory<ChangeMetadataItem>
{
    private final DataAccess dataAccess;

    @Inject
    public ChangeMetadataItemFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ChangeMetadataItem create( Object o, MappingContext mappingContext )
    {
        ChangeBatch.ChangeItem asChangeItem = ( ChangeBatch.ChangeItem ) o;
        if ( asChangeItem.getKey() != null )
        {
            return dataAccess.find( ChangeMetadataItem.class, asChangeItem.getKey() );
        }

        String metadataId = ( String ) mappingContext.getProperty( "metadataId" );
        ChangeMetadata changeMetadata = dataAccess.find( ChangeMetadata.class, metadataId );
        return new ChangeMetadataItem( changeMetadata );
    }
}
