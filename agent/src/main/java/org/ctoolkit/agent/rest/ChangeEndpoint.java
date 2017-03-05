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
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.resource.ChangeBatch;
import org.ctoolkit.agent.resource.ChangeJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for DB change
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
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

    // -- change CRUD

    @ApiMethod( name = "changeBatch.insert", path = "change", httpMethod = ApiMethod.HttpMethod.POST )
    public ChangeBatch insertChange( ChangeBatch changeBatch, User authUser )
    {
        ChangeMetadata changeMetadata = mapper.map( changeBatch, ChangeMetadata.class );
        ChangeMetadata changeMetadataBe = service.create( changeMetadata );

        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "changeBatch.update", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ChangeBatch updateChange( @Named( "id" ) String id, ChangeBatch changeBatch, User authUser ) throws Exception
    {
        if ( service.get( new MetadataKey<>( id, ExportMetadata.class ) ) == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        ChangeMetadata changeMetadata = mapper.map( changeBatch, ChangeMetadata.class );
        ChangeMetadata changeMetadataBe = service.update( changeMetadata );

        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "changeBatch.delete", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteChange( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadata = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadata == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        service.delete( changeMetadata );
    }

    @ApiMethod( name = "changeBatch.get", path = "change/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ChangeBatch getChange( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadataBe = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadataBe == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        return mapper.map( changeMetadataBe, ChangeBatch.class );
    }

    @ApiMethod( name = "changeBatch.list", path = "change", httpMethod = ApiMethod.HttpMethod.GET )
    @SuppressWarnings( "unchecked" )
    public List<ChangeBatch> listChange( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
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
                .metadataClass( ChangeMetadata.class )
                .build();

        List<ChangeMetadata> changeMetadataListBe = service.list( filter );
        List<ChangeBatch> changeBatchList = new ArrayList<>();
        for ( ChangeMetadata changeMetadataBe : changeMetadataListBe )
        {
            changeBatchList.add( mapper.map( changeMetadataBe, ChangeBatch.class ) );
        }

        return changeBatchList;
    }

    // -- change item CRUD

    @ApiMethod( name = "changeBatch.item.insert", path = "change/{metadataId}/item", httpMethod = ApiMethod.HttpMethod.POST )
    public ChangeBatch.ChangeItem insertChangeItem( @Named( "metadataId" ) String metadataId, ChangeBatch.ChangeItem changeBatchItem, User authUser )
    {
        Map<Object, Object> props = new HashMap<>();
        props.put( "metadataId", metadataId );
        MappingContext ctx = new MappingContext( props );

        ChangeMetadata changeMetadata = new ChangeMetadata();
        changeMetadata.setKey( metadataId );

        ChangeMetadataItem changeMetadataItem = mapper.map( changeBatchItem, ChangeMetadataItem.class, ctx );
        ChangeMetadataItem changeMetadataItemBe = service.create( changeMetadata, changeMetadataItem );

        return mapper.map( changeMetadataItemBe, ChangeBatch.ChangeItem.class );
    }

    @ApiMethod( name = "changeBatch.item.update", path = "change/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public ChangeBatch.ChangeItem updateChangeItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, ChangeBatch.ChangeItem changeBatchItem, User authUser )
            throws Exception
    {
        if ( service.get( new MetadataItemKey<>( id, ChangeMetadataItem.class ) ) == null )
        {
            throw new NotFoundException( "Change item not found for id: " + id );
        }

        ChangeMetadataItem changeMetadataItem = mapper.map( changeBatchItem, ChangeMetadataItem.class );
        ChangeMetadataItem changeMetadataItemBe = service.update( changeMetadataItem );

        return mapper.map( changeMetadataItemBe, ChangeBatch.ChangeItem.class );
    }

    @ApiMethod( name = "changeBatch.item.delete", path = "change/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteChangeItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ChangeMetadataItem item = service.get( new MetadataItemKey<>( id, ChangeMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Change item not found for id: " + id );
        }

        service.delete( item );
    }

    @ApiMethod( name = "changeBatch.item.get", path = "change/{metadataId}/item/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public ChangeBatch.ChangeItem getChangeItem( @Named( "metadataId" ) String metadataId, @Named( "id" ) String id, User authUser )
            throws Exception
    {
        ChangeMetadataItem item = service.get( new MetadataItemKey<>( id, ChangeMetadataItem.class ) );
        if ( item == null )
        {
            throw new NotFoundException( "Change item not found for id: " + id );
        }

        return mapper.map( item, ChangeBatch.ChangeItem.class );
    }

    // -- job CRUD

    @ApiMethod( name = "changeBatch.job.start", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.POST )
    public ChangeJob startJob( @Named( "id" ) String id, ChangeJob job, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadata = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadata == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.startJob( changeMetadata );
            return service.getJobInfo( changeMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "changeBatch.job.cancel", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.PUT )
    public ChangeJob cancelJob( @Named( "id" ) String id, ChangeJob job, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadata = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadata == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.cancelJob( changeMetadata );
            return service.getJobInfo( changeMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "changeBatch.job.delete", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void deleteJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadata = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadata == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            service.deleteJob( changeMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }

    @ApiMethod( name = "changeBatch.job.progress", path = "change/{id}/job", httpMethod = ApiMethod.HttpMethod.GET )
    public ChangeJob getJob( @Named( "id" ) String id, User authUser ) throws Exception
    {
        ChangeMetadata changeMetadata = service.get( new MetadataKey<>( id, ChangeMetadata.class ) );
        if ( changeMetadata == null )
        {
            throw new NotFoundException( "Change not found for id: " + id );
        }

        try
        {
            return service.getJobInfo( changeMetadata );
        }
        catch ( ObjectNotFoundException e )
        {
            throw new NotFoundException( e );
        }
    }
}
