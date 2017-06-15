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
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.resource.ImportBatch;
import org.ctoolkit.agent.resource.ImportJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for DB import
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
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

    @ApiMethod( name = "importBatch.insert", path = "import", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportBatch insertImport( ImportBatch importBatch, User authUser )
    {
        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.create( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @ApiMethod( name = "importBatch.update", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportBatch updateImport( @Named( "id" ) Long id, ImportBatch importBatch, User authUser ) throws Exception
    {
        if ( service.get( new MetadataKey<>( id, ImportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.update( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @ApiMethod( name = "importBatch.delete", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImport( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        service.delete( importMetadata );
    }

    @ApiMethod( name = "importBatch.get", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportBatch getImport( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        return mapper.map( importMetadata, ImportBatch.class );
    }

    @ApiMethod( name = "importBatch.list", path = "import", httpMethod = ApiMethod.HttpMethod.GET )
    @SuppressWarnings( "unchecked" )
    public List<ImportBatch> listImport( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
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

    @ApiMethod( name = "importBatch.item.insert", path = "import/{metadataId}/item", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportBatch.ImportItem insertImportItem( @Named( "metadataId" ) String metadataId, ImportBatch.ImportItem importBatchItem, User authUser )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        ImportMetadata importMetadata = new ImportMetadata();
        importMetadata.setKey( metadataId );

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class, ctx );
        ImportMetadataItem importMetadataItemBe = service.create( importMetadata, importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @ApiMethod( name = "importBatch.item.update", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportBatch.ImportItem updateImportItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, ImportBatch.ImportItem importBatchItem, User authUser )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( id, metadataId, ImportMetadataItem.class, ImportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class );
        ImportMetadataItem importMetadataItemBe = service.update( importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @ApiMethod( name = "importBatch.item.delete", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImportItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, User authUser )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>( id, metadataId, ImportMetadataItem.class, ImportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        service.delete( item );
    }

    @ApiMethod( name = "importBatch.item.get", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportBatch.ImportItem getImportItem( @Named( "metadataId" ) Long metadataId, @Named( "id" ) Long id, User authUser )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>(id, metadataId, ImportMetadataItem.class, ImportMetadata.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        return mapper.map( item, ImportBatch.ImportItem.class );
    }

    // -- job CRUD

    @ApiMethod( name = "importBatch.job.start", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportJob startImportJob( @Named( "id" ) String id, ImportJob job, User authUser ) throws Exception
    {
        ImportMetadata importMetadata = new ImportMetadata();
//        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
//        if ( importMetadata == null )
//        {
//            throw new NotFoundException( "Import not found for id: " + id );
//        }

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

    @ApiMethod( name = "importBatch.job.cancel", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportJob cancelImportJob( @Named( "id" ) Long id, ImportJob job, User authUser ) throws Exception
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

    @ApiMethod( name = "importBatch.job.delete", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImportJob( @Named( "id" ) Long id, User authUser ) throws Exception
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.deleteJob( importMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "importBatch.job.progress", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportJob getImportJob( @Named( "id" ) Long id, User authUser ) throws Exception
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
