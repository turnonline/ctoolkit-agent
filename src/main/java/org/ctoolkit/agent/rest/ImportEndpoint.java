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
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.resource.ImportBatch;
import org.ctoolkit.agent.resource.ImportJob;
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
 * Endpoint for import
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Path( "/" )
public class ImportEndpoint
{
    private ChangeSetService service;

    private MapperFacade mapper;

    public ImportEndpoint()
    {
    }

    @Inject
    public ImportEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    // -- import CRUD

    @POST
    @Path( "/import" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ImportBatch insertImport( ImportBatch importBatch )
    {
        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.create( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @PUT
    @Path( "/import/{importId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ImportBatch updateImport( @PathParam( "importId" ) Long id,
                                     ImportBatch importBatch )
            throws NotFoundException
    {
        if ( service.get( new MetadataKey<>( id, ImportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.update( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @DELETE
    @Path( "/import/{importId}" )
    public void deleteImport( @PathParam( "importId" ) Long id )
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        service.delete( importMetadata );
    }

    @GET
    @Path( "/import/{importId}" )
    @Produces( "application/json" )
    public ImportBatch getImport( @PathParam( "importId" ) Long id )
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        return mapper.map( importMetadata, ImportBatch.class );
    }

    @GET
    @Path( "/import" )
    @Produces( "application/json" )
    @SuppressWarnings( "unchecked" )
    public List<ImportBatch> listImport( @DefaultValue( "0" ) @QueryParam( "start" ) Integer start,
                                         @DefaultValue( "10" ) @QueryParam( "length" ) Integer length,
                                         @DefaultValue( "name" ) @QueryParam( "orderBy" ) String orderBy,
                                         @DefaultValue( "true" ) @QueryParam( "ascending" ) Boolean ascending )
    {
        BaseMetadataFilter filter = new BaseMetadataFilter.Builder()
                .start( start )
                .length( length )
                .orderBy( orderBy )
                .ascending( ascending )
                .metadataClass( ImportMetadata.class )
                .build();

        List<ImportMetadata> importMetadataListBe = service.list( filter );
        List<ImportBatch> importBatchList = new ArrayList<>();
        for ( ImportMetadata importMetadataBe : importMetadataListBe )
        {
            importBatchList.add( mapper.map( importMetadataBe, ImportBatch.class ) );
        }

        return importBatchList;
    }

    // -- import item CRUD

    @POST
    @Path( "/import/{importId}/item" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ImportBatch.ImportItem insertImportItem( @PathParam( "importId" ) Long metadataId,
                                                    ImportBatch.ImportItem importBatchItem )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        ImportMetadata importMetadata = service.get( new MetadataKey<>( metadataId, ImportMetadata.class ) );

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class, ctx );
        ImportMetadataItem importMetadataItemBe = service.create( importMetadata, importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @PUT
    @Path( "/import/{importId}/item/{itemId}" )
    @Consumes( "application/json" )
    @Produces( "application/json" )
    public ImportBatch.ImportItem updateImportItem( @PathParam( "importId" ) Long metadataId,
                                                    @PathParam( "itemId" ) Long itemId,
                                                    ImportBatch.ImportItem importBatchItem )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( itemId, metadataId, ImportMetadataItem.class, ImportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Import item not found for itemId: " + itemId );
        }

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class );
        ImportMetadataItem importMetadataItemBe = service.update( importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @DELETE
    @Path( "/import/{importId}/item/{itemId}" )
    public void deleteImportItem( @PathParam( "importId" ) Long metadataId,
                                  @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, ImportMetadataItem.class, ImportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for itemId: " + itemId );
        }

        service.delete( item );
    }

    @GET
    @Path( "/import/{importId}/item/{itemId}" )
    @Produces( "application/json" )
    public ImportBatch.ImportItem getImportItem( @PathParam( "importId" ) Long metadataId,
                                                 @PathParam( "itemId" ) Long itemId )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>( itemId, metadataId, ImportMetadataItem.class, ImportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for itemId: " + itemId );
        }

        return mapper.map( item, ImportBatch.ImportItem.class );
    }

    // -- job CRUD

    @POST
    @Path( "/import/{importId}/job" )
    @Produces( "application/json" )
    public ImportJob startImportJob( @PathParam( "importId" ) Long id )
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.startJob( importMetadata );
            return service.getJobInfo( importMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @DELETE
    @Path( "/import/{importId}/job" )
    @Produces( "application/json" )
    public ImportJob cancelImportJob( @PathParam( "importId" ) Long id )
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.cancelJob( importMetadata );
            return service.getJobInfo( importMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @GET
    @Path( "/import/{importId}/job" )
    @Produces( "application/json" )
    public ImportJob getImportJob( @PathParam( "importId" ) Long id )
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            return service.getJobInfo( importMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
