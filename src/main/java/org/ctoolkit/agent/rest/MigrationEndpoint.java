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

package org.ctoolkit.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.resource.MigrationBatch;
import org.ctoolkit.agent.resource.MigrationJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for migration
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
public class MigrationEndpoint
{
    private ChangeSetService service;

    private MapperFacade mapper;

    public MigrationEndpoint()
    {
    }

    @Inject
    public MigrationEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    // -- migration CRUD

    @ApiMethod( name = "migrationBatch.insert", path = "migration", httpMethod = ApiMethod.HttpMethod.POST )
    public MigrationBatch insertMigration( MigrationBatch migrationBatch, User authUser )
    {
        MigrationMetadata migrationMetadata = mapper.map( migrationBatch, MigrationMetadata.class );
        MigrationMetadata migrationMetadataBe = service.create( migrationMetadata );

        return mapper.map( migrationMetadataBe, MigrationBatch.class );
    }

    @ApiMethod( name = "migrationBatch.update", path = "migration/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public MigrationBatch updateMigration( @Named( "id" ) Long id, MigrationBatch migrationBatch, User authUser ) throws Exception
    {
        if ( service.get( new MetadataKey<>( id, MigrationMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        MigrationMetadata migrationMetadata = mapper.map( migrationBatch, MigrationMetadata.class );
        MigrationMetadata migrationMetadataBe = service.update( migrationMetadata );

        return mapper.map( migrationMetadataBe, MigrationBatch.class );
    }

    @ApiMethod( name = "migrationBatch.delete", path = "migration/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteMigration( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        service.delete( migrationMetadata );
    }

    @ApiMethod( name = "migrationBatch.get", path = "migration/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public MigrationBatch getMigration( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        return mapper.map( migrationMetadata, MigrationBatch.class );
    }

    @ApiMethod( name = "migrationBatch.list", path = "migration", httpMethod = ApiMethod.HttpMethod.GET )
    @SuppressWarnings( "unchecked" )
    public List<MigrationBatch> listMigration( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
                                         @DefaultValue( "10" ) @Nullable @Named( "length" ) Integer length,
                                         @Nullable @Named( "orderBy" ) String orderBy,
                                         @DefaultValue( "true" ) @Nullable @Named( "ascending" ) Boolean ascending,
                                         User authUser ) throws Exception
    {
        BaseMetadataFilter filter = new BaseMetadataFilter.Builder()
                .start( start )
                .length( length )
                .orderBy( orderBy )
                .ascending( ascending )
                .metadataClass( MigrationMetadata.class )
                .build();

        List<MigrationMetadata> migrationMetadataListBe = service.list( filter );
        List<MigrationBatch> migrationBatchList = new ArrayList<>();
        for ( MigrationMetadata migrationMetadataBe : migrationMetadataListBe )
        {
            migrationBatchList.add( mapper.map( migrationMetadataBe, MigrationBatch.class ) );
        }

        return migrationBatchList;
    }

    // -- migration item CRUD

    @ApiMethod( name = "migrationBatch.item.insert", path = "migration/{metadataId}/item", httpMethod = ApiMethod.HttpMethod.POST )
    public MigrationBatch.MigrationItem insertMigrationItem( @Named( "metadataId" ) Long metadataId, MigrationBatch.MigrationItem migrationBatchItem, User authUser )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( metadataId, MigrationMetadata.class ) );

        MigrationMetadataItem migrationMetadataItem = mapper.map( migrationBatchItem, MigrationMetadataItem.class, ctx );
        MigrationMetadataItem migrationMetadataItemBe = service.create( migrationMetadata, migrationMetadataItem );

        return mapper.map( migrationMetadataItemBe, MigrationBatch.MigrationItem.class );
    }

    @ApiMethod( name = "migrationBatch.item.update", path = "migration/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public MigrationBatch.MigrationItem updateMigrationItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, MigrationBatch.MigrationItem migrationBatchItem, User authUser )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( id, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Migration item not found for id: " + id );
        }

        MigrationMetadataItem migrationMetadataItem = mapper.map( migrationBatchItem, MigrationMetadataItem.class );
        MigrationMetadataItem migrationMetadataItemBe = service.update( migrationMetadataItem );

        return mapper.map( migrationMetadataItemBe, MigrationBatch.MigrationItem.class );
    }

    @ApiMethod( name = "migrationBatch.item.delete", path = "migration/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteMigrationItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, User authUser )
            throws Exception
    {
        MigrationMetadataItem item = service.get( new MetadataItemKey<>( id, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Migration item not found for id: " + id );
        }

        service.delete( item );
    }

    @ApiMethod( name = "migrationBatch.item.get", path = "migration/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public MigrationBatch.MigrationItem getMigrationItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, User authUser )
            throws Exception
    {
        MigrationMetadataItem item = service.get( new MetadataItemKey<>(id, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Migration item not found for id: " + id );
        }

        return mapper.map( item, MigrationBatch.MigrationItem.class );
    }

    // -- job CRUD

    @ApiMethod( name = "migrationBatch.job.start", path = "migration/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public MigrationJob startMigrationJob( @Named( "id" ) Long id, MigrationJob job, User authUser ) throws Exception
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        try
        {
            service.startJob( migrationMetadata );
            return service.getJobInfo( migrationMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "migrationBatch.job.cancel", path = "migration/{id}/job", httpMethod = ApiMethod.HttpMethod.PUT )
    public MigrationJob cancelMigrationJob( @Named( "id" ) Long id, MigrationJob job, User authUser ) throws Exception
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        try
        {
            service.cancelJob( migrationMetadata );
            return service.getJobInfo( migrationMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "migrationBatch.job.progress", path = "migration/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public MigrationJob getMigrationJob( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        try
        {
            return service.getJobInfo( migrationMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
