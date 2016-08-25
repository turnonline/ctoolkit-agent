package org.ctoolkit.migration.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.model.ExportBatch;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;

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

    @ApiMethod( name = "export.create", path = "export", httpMethod = ApiMethod.HttpMethod.POST )
    public ExportBatch createExport( ExportBatch exportBatch, User authUser )
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

    @ApiMethod( name = "export.job.start", path = "export/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public JobInfo startJob( @Named( "id" ) String id, User authUser ) throws Exception
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
    public JobInfo cancelJob( @Named( "id" ) String id, User authUser ) throws Exception
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
    public JobInfo getJob( @Named( "id" ) String id, User authUser ) throws Exception
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
