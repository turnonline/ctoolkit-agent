package org.ctoolkit.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.model.ChangeBatch;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Endpoint for DB change
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
// TODO: resolve security issue
public class ChangeEndpoint
{
    private final ChangeSetService service;

    private final MapperFacade mapper;

    @Inject
    public ChangeEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    @ApiMethod( name = "change.create", path = "change", httpMethod = ApiMethod.HttpMethod.POST )
    public ChangeBatch createChange( ChangeBatch changeBatch, User authUser )
    {
        ChangeMetadata changeMetadata = mapper.map( changeBatch, ChangeMetadata.class );
        ChangeMetadata changeMetadataBe = service.createChangeMetadata( changeMetadata );

        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "change.update", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ChangeBatch updateChange( @Named( "id" ) String id, ChangeBatch changeBatch, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        ChangeMetadata changeMetadata = mapper.map( changeBatch, ChangeMetadata.class );
        ChangeMetadata changeMetadataBe = service.updateChangeMetadata( changeMetadata );

        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "change.delete", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteChange( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        service.deleteChangeMetadata( id );
    }

    @ApiMethod( name = "change.get", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ChangeBatch getChange( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        ChangeMetadata changeMetadataBe = service.getChangeMetadata( id );
        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "change.job.start", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public JobInfo startJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.startChangeJob( id );
            return service.getChangeJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "change.job.cancel", path = "change/{id}/job/cancel", httpMethod = ApiMethod.HttpMethod.PUT )
    public JobInfo cancelJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.cancelChangeJob( id );
            return service.getChangeJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "change.job.delete", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.deleteChangeJob( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "change.job.progress", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public JobInfo getJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        if ( service.getChangeMetadata( id ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            return service.getChangeJobInfo( id );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
