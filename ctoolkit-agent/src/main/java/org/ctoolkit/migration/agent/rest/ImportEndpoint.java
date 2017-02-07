package org.ctoolkit.migration.agent.rest;

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
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.model.BaseMetadataFilter;
import org.ctoolkit.migration.agent.model.ImportBatch;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.model.MetadataItemKey;
import org.ctoolkit.migration.agent.model.MetadataKey;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for DB import
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
public class ImportEndpoint
{
    private final ChangeSetService service;

    private final MapperFacade mapper;

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
    public ImportBatch updateImport( @Named( "id" ) String id, ImportBatch importBatch, User authUser ) throws Exception
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
    public void deleteImport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ImportMetadata importMetadata = service.get( new MetadataKey<>( id, ImportMetadata.class ) );
        if ( importMetadata == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        service.delete( importMetadata );
    }

    @ApiMethod( name = "importBatch.get", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportBatch getImport( @Named( "id" ) String id, User authUser ) throws Exception
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

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class, ctx );
        ImportMetadataItem importMetadataItemBe = service.create( importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @ApiMethod( name = "importBatch.item.update", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportBatch.ImportItem updateImportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, ImportBatch.ImportItem importBatchItem, User authUser )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( id, ImportMetadataItem.class ) ) == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        ImportMetadataItem importMetadataItem = mapper.map( importBatchItem, ImportMetadataItem.class );
        ImportMetadataItem importMetadataItemBe = service.update( importMetadataItem );

        return mapper.map( importMetadataItemBe, ImportBatch.ImportItem.class );
    }

    @ApiMethod( name = "importBatch.item.delete", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>( id, ImportMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        service.delete( item );
    }

    @ApiMethod( name = "importBatch.item.get", path = "import/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportBatch.ImportItem getImportItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ImportMetadataItem item = service.get( new MetadataItemKey<>( id, ImportMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Import item not found for id: " + id );
        }

        return mapper.map( item, ImportBatch.ImportItem.class );
    }

    // -- job CRUD

    @ApiMethod( name = "importBatch.job.start", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportJobInfo startJob( @Named( "id" ) String id, User authUser ) throws Exception
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

    @ApiMethod( name = "importBatch.job.cancel", path = "import/{id}/job/cancel", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportJobInfo cancelJob( @Named( "id" ) String id, User authUser ) throws Exception
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
    public void deleteJob( @Named( "id" ) String id, User authUser ) throws Exception
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
    public ImportJobInfo getJob( @Named( "id" ) String id, User authUser ) throws Exception
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
