package org.ctoolkit.migration.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.migration.agent.exception.ObjectNotFoundException;
import org.ctoolkit.migration.agent.model.ImportBatch;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Endpoint for DB import
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
// TODO: resolve security issue
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

    @ApiMethod( name = "import.create", path = "import", httpMethod = ApiMethod.HttpMethod.POST )
    public ImportBatch createImport( ImportBatch importBatch, User authUser )
    {
        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.createImportMetadata( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @ApiMethod( name = "import.update", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ImportBatch updateImport( @Named( "id" ) String id, ImportBatch importBatch, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        ImportMetadata importMetadata = mapper.map( importBatch, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.updateImportMetadata( importMetadata );

        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @ApiMethod( name = "import.delete", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        service.deleteImportMetadata( id );
    }

    @ApiMethod( name = "import.get", path = "import/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ImportBatch getImport( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        ImportMetadata importMetadataBe = service.getImportMetadata( id );
        return mapper.map( importMetadataBe, ImportBatch.class );
    }

    @ApiMethod( name = "import.job.start", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public JobInfo startJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.startImportJob( id );
            return service.getImportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.cancel", path = "import/{id}/job/cancel", httpMethod = ApiMethod.HttpMethod.PUT )
    public JobInfo cancelJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.cancelImportJob( id );
            return service.getImportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.delete", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            service.deleteImportJob( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.progress", path = "import/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public JobInfo getJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( id ) == null )
        {
            throw new NotFoundException( "Import not found for id: " + id );
        }

        try
        {
            return service.getImportJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
