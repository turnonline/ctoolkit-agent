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

import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.resource.MigrationBatch;
import org.ctoolkit.agent.resource.MigrationJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link MigrationBatch} to {@link MigrationMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrationToMigrationMetadataMapper
        extends BaseSetToBaseMetadataMapper<MigrationBatch, MigrationMetadata, MigrationBatch.MigrationItem, MigrationMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

    @Override
    protected MigrationBatch.MigrationItem newItem()
    {
        return new MigrationBatch.MigrationItem();
    }

    @Override
    protected void addItem( MigrationBatch anImport, MigrationBatch.MigrationItem anItem )
    {
        anImport.getItems().add( anItem );
    }

    @Override
    protected void extraMapBToA( MigrationMetadata metadata, MigrationBatch set )
    {
        set.setJobInfo( ( MigrationJob ) changeSetService.getJobInfo( metadata ) );

        set.setSource( metadata.getSource() );
        set.setTarget( metadata.getTarget() );
    }

    @Override
    protected void extraMapAToB( MigrationBatch set, MigrationMetadata metadata )
    {
        super.extraMapAToB( set, metadata );

        metadata.setSource( set.getSource() );
        metadata.setTarget( set.getTarget() );
    }
}
