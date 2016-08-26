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
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.model.ExportBatch;
import org.ctoolkit.migration.agent.model.ExportJobInfo;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.Filter;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint for DB export
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
// TODO: resolve security issue
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

    @ApiMethod( name = "export.insert", path = "export", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportBatch insertExport( ExportBatch exportBatch, User authUser )
    {
        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.createExportMetadata( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "export.update", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ExportBatch updateExport( @Named( "id" ) String id, ExportBatch exportBatch, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        ExportMetadata exportMetadata = mapper.map( exportBatch, ExportMetadata.class );
        ExportMetadata exportMetadataBe = service.updateExportMetadata( exportMetadata );

        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "export.delete", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteExport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        service.deleteExportMetadata( id );
    }

    @ApiMethod( name = "export.get", path = "export/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ExportBatch getExport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        ExportMetadata exportMetadataBe = service.getExportMetadata( id );
        return mapper.map( exportMetadataBe, ExportBatch.class );
    }

    @ApiMethod( name = "export.list", path = "export", httpMethod = ApiMethod.HttpMethod.GET )
    public List<ExportBatch> listImport( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
                                         @DefaultValue( "10" ) @Nullable @Named( "length" ) Integer length,
                                         @Nullable @Named( "orderBy" ) String orderBy,
                                         @DefaultValue( "true" ) @Nullable @Named( "ascending" ) Boolean ascending,
                                         User authUser ) throws Exception
    {
        Filter filter = new Filter.Builder<>()
                .start( start )
                .length( length )
                .orderBy( orderBy )
                .ascending( ascending )
                .build();

        List<ExportMetadata> exportMetadataListBe = service.getExportMetadataList( filter );
        List<ExportBatch> exportBatchList = new ArrayList<>();
        for ( ExportMetadata exportMetadataBe : exportMetadataListBe )
        {
            exportBatchList.add( mapper.map( exportMetadataBe, ExportBatch.class ) );
        }

        return exportBatchList;
    }

    // -- job CRUD

    @ApiMethod( name = "export.job.start", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportJobInfo startJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.startExportJob( id );
            return service.getExportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "export.job.cancel", path = "export/{id}/job/cancel", httpMethod = ApiMethod.HttpMethod.PUT )
    public ExportJobInfo cancelJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.cancelExportJob( id );
            return service.getExportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "export.job.delete", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            service.deleteExportJob( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "export.job.progress", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public ExportJobInfo getJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getExportMetadata( id ) == null )
        {
            throw new NotFoundException( "Export not found for id: " + id );
        }

        try
        {
            return service.getExportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
