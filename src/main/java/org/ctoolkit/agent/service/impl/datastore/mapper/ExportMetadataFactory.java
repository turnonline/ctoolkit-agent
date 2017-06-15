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

import com.google.cloud.datastore.Key;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.resource.ExportBatch;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ExportMetadataFactory
        implements ObjectFactory<ExportMetadata>
{
    private final DataAccess dataAccess;

    private final String projectId;

    @Inject
    public ExportMetadataFactory( DataAccess dataAccess, @ProjectId String projectId )
    {
        this.dataAccess = dataAccess;
        this.projectId = projectId;
    }

    @Override
    public ExportMetadata create( Object o, MappingContext mappingContext )
    {
        ExportBatch asExport = ( ExportBatch ) o;
        if ( asExport.getId() != null )
        {
            String kind = ExportMetadata.class.getAnnotation( EntityMarker.class ).name();
            Long id = asExport.getId();

            return dataAccess.find( ExportMetadata.class, Key.newBuilder( projectId, kind, id ).build() );
        }

        return new ExportMetadata();
    }
}
