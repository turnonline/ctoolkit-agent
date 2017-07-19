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
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.resource.ExportBatch;
import org.ctoolkit.agent.resource.ExportJob;
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
 * Endpoint for export
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Path( "/" )
public class ExportEndpoint
{
    private ChangeSetService service;

    private MapperFacade mapper;

    public ExportEndpoint()
    {
    }

    @Inject
    public ExportEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    // -- export CRUD

    @POST
    @Path( "/export" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ExportBatch insertExport( ExportBatch exportBatch )
    {
        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.create( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @PUT
    @Path( "/export/{exportId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ExportBatch updateExport( @PathParam( "exportId" ) Long id,
                                     ExportBatch exportBatch )
    {
        if ( service.get( new MetadataKey<>( id, ExportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.update( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @DELETE
    @Path( "/export/{exportId}" )
    public void deleteExport( @PathParam( "exportId" ) Long id )
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        service.delete( exportMetadata );
    }

    @GET
    @Path( "/export/{exportId}" )
    @Produces( "application/json" )
    public ExportBatch getExport( @PathParam( "exportId" ) Long id )
    {
        ExportMetadata exportMetadataBe = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadataBe == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @GET
    @Path( "/export" )
    @Produces( "application/json" )
    @SuppressWarnings( "unchecked" )
    public List<ExportBatch> listExport( @DefaultValue( "0" ) @QueryParam( "start" ) Integer start,
                                         @DefaultValue( "10" ) @QueryParam( "length" ) Integer length,
                                         @DefaultValue( "name" ) @QueryParam( "orderBy" ) String orderBy,
                                         @DefaultValue( "true" ) @QueryParam( "ascending" ) Boolean ascending )
    {
        BaseMetadataFilter filter = new BaseMetadataFilter.Builder<>()
                .start( start )
                .length( length )
                .orderBy( orderBy )
                .ascending( ascending )
                .metadataClass( ExportMetadata.class )
                .build();

        List<ExportMetadata> exportMetadataListBe = service.list( filter );
        List<ExportBatch> exportBatchList = new ArrayList<>();
        for ( ExportMetadata exportMetadataBe : exportMetadataListBe )
        {
            exportBatchList.add( mapper.map( exportMetadataBe, ExportBatch.class ) );
        }

        return exportBatchList;
    }

    // -- export item CRUD

    @POST
    @Path( "/export/{exportId}/item" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ExportBatch.ExportItem insertExportItem( @PathParam( "exportId" ) Long metadataId,
                                                    ExportBatch.ExportItem exportBatchItem )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        ExportMetadata exportMetadata = service.get( new MetadataKey<>( metadataId, ExportMetadata.class ) );

        ExportMetadataItem exportMetadataItem = mapper.map( exportBatchItem, ExportMetadataItem.class, ctx );
        ExportMetadataItem exportMetadataItemBe = service.create( exportMetadata, exportMetadataItem );

        return mapper.map( exportMetadataItemBe, ExportBatch.ExportItem.class );
    }

    @PUT
    @Path( "/export/{exportId}/item/{itemId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ExportBatch.ExportItem updateExportItem( @PathParam( "exportId" ) Long metadataId,
                                                    @PathParam( "itemId" ) Long itemId,
                                                    ExportBatch.ExportItem exportBatchItem )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( itemId, metadataId, ExportMetadataItem.class, ExportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Export item not found for itemId: " + itemId );
        }

        ExportMetadataItem exportMetadataItem = mapper.map( exportBatchItem, ExportMetadataItem.class );
        ExportMetadataItem exportMetadataItemBe = service.update( exportMetadataItem );

        return mapper.map( exportMetadataItemBe, ExportBatch.ExportItem.class );
    }

    @DELETE
    @Path( "/export/{exportId}/item/{itemId}" )
    public void deleteExportItem( @PathParam( "exportId" ) Long metadataId,
                                  @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        ExportMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, ExportMetadataItem.class, ExportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Export item not found for itemId: " + itemId );
        }

        service.delete( item );
    }

    @GET
    @Path( "/export/{exportId}/item/{itemId}" )
    @Produces( "application/json" )
    public ExportBatch.ExportItem getExportItem( @PathParam( "exportId" ) Long metadataId,
                                                 @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        ExportMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, ExportMetadataItem.class, ExportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Export item not found for itemId: " + itemId );
        }

        return mapper.map( item, ExportBatch.ExportItem.class );
    }

    // -- job CRUD

    @POST
    @Path( "/export/{exportId}/job" )
    @Produces( "application/json" )
    public ExportJob startExportJob( @PathParam( "exportId" ) Long id )
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.startJob( exportMetadata );
            return service.getJobInfo( exportMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @DELETE
    @Path( "/export/{exportId}/job" )
    @Produces( "application/json" )
    public ExportJob cancelExportJob( @PathParam( "exportId" ) Long id )
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.cancelJob( exportMetadata );
            return service.getJobInfo( exportMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @GET
    @Path( "/export/{exportId}/job" )
    @Produces( "application/json" )
    public ExportJob getExportJob( @PathParam( "exportId" ) Long id ) throws Exception
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            return service.getJobInfo( exportMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
