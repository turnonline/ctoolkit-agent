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
import com.google.appengine.tools.mapreduce.OutputWriter;
import com.google.appengine.tools.mapreduce.inputs.MetadataItemShardInput;
import com.google.appengine.tools.mapreduce.outputs.NoOutput;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.agent.model.MigrationJobConfiguration;
import org.ctoolkit.restapi.client.Identifier;
import org.ctoolkit.restapi.client.RequestCredential;
import org.ctoolkit.restapi.client.ResourceFacade;
import org.ctoolkit.restapi.client.agent.model.ImportJobInfo;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrateJobMapSpecificationProvider
        implements MapSpecificationProvider, Serializable
{
    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private static final long serialVersionUID = 8477680668820034478L;

    @Inject
    private static Injector injector;

    private final MigrationJobConfiguration jobConfiguration;

    private final String agentUrl;

    private final String token;

    private final MigrateMapOnlyMapperJob mapper;

    /**
     * field injection to bypass serialization issue
     */
    @Inject
    private transient ResourceFacade facade;

    @Inject
    public MigrateJobMapSpecificationProvider( @Assisted MigrationJobConfiguration jobConfiguration,
                                               @Assisted( "agentUrl" ) String agentUrl,
                                               @Assisted( "token" ) String token,
                                               MigrateMapOnlyMapperJob mapper )
    {
        this.jobConfiguration = jobConfiguration;
        this.agentUrl = agentUrl;
        this.token = token;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MapSpecification<Entity, Entity, Entity> get()
    {
        MetadataItemShardInput input = new MetadataItemShardInput( "_ExportMetadataItem", jobConfiguration.getExportId(), SHARD_COUNT );

        return new MapSpecification.Builder<>( input, mapper, new NoOutput<Entity, Entity>()
        {
            @Override
            public Entity finish( Collection<? extends OutputWriter<Entity>> outputWriters )
            {
                injector.injectMembers( MigrateJobMapSpecificationProvider.this );

                // start job to import data
                RequestCredential credential = new RequestCredential();
                credential.setApiKey( token );
                credential.setEndpointUrl( agentUrl );

                try
                {
                    Identifier parent = new Identifier( jobConfiguration.getImportId() );
                    facade.insert( new ImportJobInfo(), parent ).config( credential ).execute();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( "Unable to start import job", e );
                }

                return null;
            }
        } ).setJobName( "MigrationJob" ).build();
    }
}
