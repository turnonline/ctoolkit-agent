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

import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.resource.ExportBatch;
import org.ctoolkit.agent.resource.ExportJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ExportBatch} to {@link ExportMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ExportToExportMetadataMapper
        extends BaseSetToBaseMetadataMapper<ExportBatch, ExportMetadata, ExportBatch.ExportItem, ExportMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

    @Override
    protected ExportBatch.ExportItem newItem()
    {
        return new ExportBatch.ExportItem();
    }

    @Override
    protected void addItem( ExportBatch anImport, ExportBatch.ExportItem anItem )
    {
        anImport.getItems().add( anItem );
    }

    @Override
    protected void extraMapBToA( ExportMetadata metadata, ExportBatch set )
    {
        set.setJobInfo( ( ExportJob ) changeSetService.getJobInfo( metadata ) );
    }

    @Override
    protected void extraMapAItemToBItem( ExportBatch.ExportItem anItem, ExportMetadataItem item )
    {
        item.setEntityToExport( anItem.getEntityToExport() );
    }

    @Override
    protected void extraMapBItemToAItem( ExportMetadataItem item, ExportBatch.ExportItem anItem )
    {
        anItem.setEntityToExport( item.getEntityToExport() );
    }
}
