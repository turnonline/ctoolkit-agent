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

import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.resource.ChangeBatch;
import org.ctoolkit.agent.resource.ChangeJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ChangeBatch} to {@link ChangeMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeToChangeMetadataMapper
        extends BaseSetToBaseMetadataMapper<ChangeBatch, ChangeMetadata, ChangeBatch.ChangeItem, ChangeMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

    @Override
    protected ChangeBatch.ChangeItem newItem()
    {
        return new ChangeBatch.ChangeItem();
    }

    @Override
    protected void addItem( ChangeBatch anImport, ChangeBatch.ChangeItem anItem )
    {
        anImport.getItems().add( anItem );
    }

    @Override
    protected void extraMapBToA( ChangeMetadata metadata, ChangeBatch set )
    {
        set.setJobInfo( ( ChangeJob ) changeSetService.getJobInfo( metadata ) );
    }
}
