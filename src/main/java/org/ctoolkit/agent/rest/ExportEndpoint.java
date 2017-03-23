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
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.resource.ExportBatch;
import org.ctoolkit.agent.resource.ExportJob;
import org.ctoolkit.agent.resource.ImportBatch;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for DB export
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
public class ExportEndpoint
{
    private final ChangeSetService service;

    private final MapperFacade mapper;

    @Inject
    public ExportEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    // -- export CRUD

    @ApiMethod( name = "exportBatch.insert", path = "export", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportBatch insertExport( ExportBatch exportBatch, User authUser )
    {
        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.create( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "exportBatch.update", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ExportBatch updateExport( @Named( "id" ) String id, ExportBatch exportBatch, User authUser ) throws Exception
    {
        if ( service.get( new MetadataKey<>( id, ExportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.update( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "exportBatch.delete", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteExport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        service.delete( exportMetadata );
    }

    @ApiMethod( name = "exportBatch.get", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ExportBatch getExport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ExportMetadata exportMetadataBe = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadataBe == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "exportBatch.list", path = "export", httpMethod = ApiMethod.HttpMethod.GET )
    @SuppressWarnings( "unchecked" )
    public List<ExportBatch> listExport( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
                                         @DefaultValue( "10" ) @Nullable @Named( "length" ) Integer length,
                                         @Nullable @Named( "orderBy" ) String orderBy,
                                         @DefaultValue( "true" ) @Nullable @Named( "ascending" ) Boolean ascending,
                                         User authUser ) throws Exception
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

    @ApiMethod( name = "exportBatch.item.insert", path = "export/{metadataId}/item", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportBatch.ExportItem insertExportItem( @Named( "metadataId" ) String metadataId, ExportBatch.ExportItem exportBatchItem, User authUser )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        ExportMetadata exportMetadata = new ExportMetadata();
        exportMetadata.setKey( metadataId );

        ExportMetadataItem exportMetadataItem = mapper.map( exportBatchItem, ExportMetadataItem.class, ctx );
        ExportMetadataItem exportMetadataItemBe = service.create( exportMetadata, exportMetadataItem );

        return mapper.map( exportMetadataItemBe, ExportBatch.ExportItem.class );
    }

    @ApiMethod( name = "exportBatch.item.update", path = "export/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ExportBatch.ExportItem updateExportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, ExportBatch.ExportItem exportBatchItem, User authUser )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( id, ExportMetadataItem.class ) ) == null )
        {
            throw new NotFoundException( "Export item not found for id: " + id );
        }

        ExportMetadataItem exportMetadataItem = mapper.map( exportBatchItem, ExportMetadataItem.class );
        ExportMetadataItem exportMetadataItemBe = service.update( exportMetadataItem );

        return mapper.map( exportMetadataItemBe, ExportBatch.ExportItem.class );
    }

    @ApiMethod( name = "exportBatch.item.delete", path = "export/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteExportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ExportMetadataItem item = service.get( new MetadataItemKey<>( id, ExportMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Export item not found for id: " + id );
        }

        service.delete( item );
    }

    @ApiMethod( name = "exportBatch.item.get", path = "export/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ExportBatch.ExportItem getExportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ExportMetadataItem item = service.get( new MetadataItemKey<>( id, ExportMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Export item not found for id: " + id );
        }

        return mapper.map( item, ExportBatch.ExportItem.class );
    }

    // -- job CRUD

    @ApiMethod( name = "exportBatch.job.start", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportJob startExportJob( @Named( "id" ) String id, ExportJob job, User authUser ) throws Exception
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

    @ApiMethod( name = "exportBatch.job.cancel", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.PUT )
    public ExportJob cancelExportJob( @Named( "id" ) String id, ExportJob job, User authUser ) throws Exception
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

    @ApiMethod( name = "exportBatch.job.delete", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteExportJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.deleteJob( exportMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "exportBatch.job.progress", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public ExportJob getExportJob( @Named( "id" ) String id, User authUser ) throws Exception
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

    // -- migration

    @ApiMethod( name = "exportBatch.migrate.insert", path = "export/{id}/migrate", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportBatch insertMigration( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ExportMetadata exportMetadata = service.get( new MetadataKey<>( id, ExportMetadata.class ) );
        if ( exportMetadata == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        ImportMetadata importMetadataBe = service.migrate( exportMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }
}