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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for migration
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Path( "/" )
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

    @POST
    @Path( "/migration" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public MigrationBatch insertMigration( MigrationBatch migrationBatch )
    {
        MigrationMetadata migrationMetadata = mapper.map( migrationBatch, MigrationMetadata.class );
        MigrationMetadata migrationMetadataBe = service.create( migrationMetadata );

        return mapper.map( migrationMetadataBe, MigrationBatch.class );
    }

    @PUT
    @Path( "/migration/{migrationId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public MigrationBatch updateMigration( @PathParam( "migrationId" ) Long id,
                                           MigrationBatch migrationBatch )
    {
        if ( service.get( new MetadataKey<>( id, MigrationMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        MigrationMetadata migrationMetadata = mapper.map( migrationBatch, MigrationMetadata.class );
        MigrationMetadata migrationMetadataBe = service.update( migrationMetadata );

        return mapper.map( migrationMetadataBe, MigrationBatch.class );
    }

    @DELETE
    @Path( "/migration/{migrationId}" )
    public void deleteMigration( @PathParam( "migrationId" ) Long id )
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        service.delete( migrationMetadata );
    }

    @GET
    @Path( "/migration/{migrationId}" )
    @Produces( "application/json" )
    public MigrationBatch getMigration( @PathParam( "migrationId" ) Long id )
    {
        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( id, MigrationMetadata.class ) );
        if ( migrationMetadata == null )
        {
            throw new NotFoundException( "Migration not found for id: " + id );
        }

        return mapper.map( migrationMetadata, MigrationBatch.class );
    }

    @GET
    @Path( "/migration" )
    @Produces( "application/json" )
    @SuppressWarnings( "unchecked" )
    public List<MigrationBatch> listMigration( @DefaultValue( "0" ) @QueryParam( "start" ) Integer start,
                                               @DefaultValue( "10" ) @QueryParam( "length" ) Integer length,
                                               @DefaultValue( "name" ) @QueryParam( "orderBy" ) String orderBy,
                                               @DefaultValue( "true" ) @QueryParam( "ascending" ) Boolean ascending )
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

    @POST
    @Path( "/migration/{migrationId}/item" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public MigrationBatch.MigrationItem insertMigrationItem( @PathParam( "migrationId" ) Long metadataId,
                                                             MigrationBatch.MigrationItem migrationBatchItem )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        MigrationMetadata migrationMetadata = service.get( new MetadataKey<>( metadataId, MigrationMetadata.class ) );

        MigrationMetadataItem migrationMetadataItem = mapper.map( migrationBatchItem, MigrationMetadataItem.class, ctx );
        MigrationMetadataItem migrationMetadataItemBe = service.create( migrationMetadata, migrationMetadataItem );

        return mapper.map( migrationMetadataItemBe, MigrationBatch.MigrationItem.class );
    }

    @PUT
    @Path( "/migration/{migrationId}/item/{itemId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public MigrationBatch.MigrationItem updateMigrationItem( @PathParam( "migrationId" ) Long metadataId,
                                                             @PathParam( "itemId" ) Long itemId,
                                                             MigrationBatch.MigrationItem migrationBatchItem )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( itemId, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Migration item not found for itemId: " + itemId );
        }

        MigrationMetadataItem migrationMetadataItem = mapper.map( migrationBatchItem, MigrationMetadataItem.class );
        MigrationMetadataItem migrationMetadataItemBe = service.update( migrationMetadataItem );

        return mapper.map( migrationMetadataItemBe, MigrationBatch.MigrationItem.class );
    }

    @DELETE
    @Path( "/migration/{migrationId}/item/{itemId}" )
    public void deleteMigrationItem( @PathParam( "migrationId" ) Long metadataId,
                                     @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        MigrationMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Migration item not found for itemId: " + itemId );
        }

        service.delete( item );
    }

    @GET
    @Path( "/migration/{migrationId}/item/{itemId}" )
    @Produces( "application/json" )
    public MigrationBatch.MigrationItem getMigrationItem( @PathParam( "migrationId" ) Long metadataId,
                                                          @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        MigrationMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, MigrationMetadataItem.class, MigrationMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Migration item not found for itemId: " + itemId );
        }

        return mapper.map( item, MigrationBatch.MigrationItem.class );
    }

    // -- job CRUD

    @POST
    @Path( "/migration/{migrationId}/job" )
    @Produces( "application/json" )
    public MigrationJob startMigrationJob( @PathParam( "migrationId" ) Long id )
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

    @DELETE
    @Path( "/migration/{migrationId}/job" )
    @Produces( "application/json" )
    public MigrationJob cancelMigrationJob( @PathParam( "migrationId" ) Long id )
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

    @GET
    @Path( "/migration/{migrationId}/job" )
    @Produces( "application/json" )
    public MigrationJob getMigrationJob( @PathParam( "migrationId" ) Long id )
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
