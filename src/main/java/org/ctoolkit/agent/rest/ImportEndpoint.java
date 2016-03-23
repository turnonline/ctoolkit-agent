package org.ctoolkit.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.model.Import;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.service.ChangeSetService;

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
    public Import createImport( Import importData, User authUser )
    {
        ImportMetadata importMetadata = mapper.map( importData, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.createImportMetadata( importMetadata );

        return mapper.map( importMetadataBe, Import.class );
    }

    @ApiMethod( name = "import.update", path = "import/{key}", httpMethod = ApiMethod.HttpMethod.PUT )
    public Import updateImport( @Named( "key" ) String key, Import importData, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        ImportMetadata importMetadata = mapper.map( importData, ImportMetadata.class );
        ImportMetadata importMetadataBe = service.updateImportMetadata( importMetadata );

        return mapper.map( importMetadataBe, Import.class );
    }

    @ApiMethod( name = "import.delete", path = "import/{key}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteImport( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        service.deleteImportMetadata( key );
    }

    @ApiMethod( name = "import.get", path = "import/{key}", httpMethod = ApiMethod.HttpMethod.GET )
    public Import getImport( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        ImportMetadata importMetadataBe = service.getImportMetadata( key );
        return mapper.map( importMetadataBe, Import.class );
    }

    @ApiMethod( name = "import.job.start", path = "import/{key}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public JobInfo startJob( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        try
        {
            service.startImportJob( key );
            return service.getJobInfo( key );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.cancel", path = "import/{key}/job/cancel", httpMethod = ApiMethod.HttpMethod.PUT )
    public JobInfo cancelJob( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        try
        {
            service.cancelImportJob( key );
            return service.getJobInfo( key );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.delete", path = "import/{key}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteJob( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        try
        {
            service.deleteImportJob( key );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "import.job.progress", path = "import/{key}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public JobInfo getJob( @Named( "key" ) String key, User authUser ) throws Exception
    {
        if ( service.getImportMetadata( key ) == null )
        {
            throw new NotFoundException( "Import not found for key: " + key );
        }

        try
        {
            return service.getJobInfo( key );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
