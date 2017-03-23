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

package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.inputs.MetadataItemShardInput;
import com.google.appengine.tools.mapreduce.outputs.NoOutput;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportJobMapSpecificationProvider
        implements MapSpecificationProvider
{
    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private final String parentKey;

    private final ImportMapOnlyMapperJob mapper;

    @Inject
    public ImportJobMapSpecificationProvider( @Assisted String parentKey, ImportMapOnlyMapperJob mapper )
    {
        this.parentKey = parentKey;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MapSpecification<Entity, Entity, Entity> get()
    {
        MetadataItemShardInput input = new MetadataItemShardInput( "_ImportMetadataItem", parentKey, SHARD_COUNT );

        return new MapSpecification.Builder<>( input, mapper, new NoOutput<Entity, Entity>() )
                .setJobName( "ImportJob" )
                .build();
    }
}
