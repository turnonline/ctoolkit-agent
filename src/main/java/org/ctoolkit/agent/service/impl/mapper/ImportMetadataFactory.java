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
import com.google.cloud.datastore.Key;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.resource.ImportBatch;

import javax.inject.Inject;

/**
 * Factory for {@link ImportMetadata}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportMetadataFactory
        implements ObjectFactory<ImportMetadata>
{
    private final Datastore datastore;

    private final String projectId;

    @Inject
    public ImportMetadataFactory( Datastore datastore, @ProjectId String projectId )
    {
        this.datastore = datastore;
        this.projectId = projectId;
    }

    @Override
    public ImportMetadata create( Object o, MappingContext mappingContext )
    {
        ImportBatch asImport = ( ImportBatch ) o;
        if ( asImport.getId() != null )
        {
            String kind = ImportMetadata.class.getAnnotation( EntityMarker.class ).name();
            Long id = asImport.getId();

            Key key = Key.newBuilder( projectId, kind, id ).build();
            return ModelConverter.convert( ImportMetadata.class, datastore.get( key ) );
        }

        return new ImportMetadata();
    }
}
