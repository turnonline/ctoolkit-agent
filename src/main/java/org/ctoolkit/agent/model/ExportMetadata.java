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

package org.ctoolkit.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Export metadata entity.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Entity( name = "_ExportMetadata" )
public class ExportMetadata
        extends BaseMetadata<ExportMetadataItem>
{
    private String mapReduceMigrationJobId;

    @Override
    protected ExportMetadataItem newItem()
    {
        return new ExportMetadataItem( this );
    }

    public String getMapReduceMigrationJobId()
    {
        return mapReduceMigrationJobId;
    }

    public void setMapReduceMigrationJobId( String mapReduceMigrationJobId )
    {
        this.mapReduceMigrationJobId = mapReduceMigrationJobId;
    }

    @Override
    public String toString()
    {
        return "ExportMetadata{" +
                "mapReduceMigrationJobId='" + mapReduceMigrationJobId + '\'' +
                "} " + super.toString();
    }
}