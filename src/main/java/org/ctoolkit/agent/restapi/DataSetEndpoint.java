package org.ctoolkit.agent.restapi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import org.ctoolkit.agent.dataset.processor.DataSetProcessor;
import org.ctoolkit.agent.restapi.resource.DataSetExport;
import org.ctoolkit.agent.restapi.resource.DataSetUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * The data set REST API endpoint.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
public class DataSetEndpoint
{
    private static final Logger log = LoggerFactory.getLogger( DataSetEndpoint.class );

    private final DataSetProcessor processor;

    @Inject
    public DataSetEndpoint( DataSetProcessor processor )
    {
        this.processor = processor;
    }

    @ApiMethod( name = "dataset.upgrade.get", path = "dataset.upgrade/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public DataSetUpgrade getUpgrade( @Named( "id" ) Long id, com.google.appengine.api.users.User authUser )
    {
        DataSetUpgrade upgrade = new DataSetUpgrade( id );

        upgrade.setCompletedAt( new Date() );
        upgrade.setCompleted( true );
        upgrade.setDataSetId( 1L );

        log.info( "User: " + authUser );

        return upgrade;
    }

    @ApiMethod( name = "dataset.upgrade.insert", path = "dataset.upgrade", httpMethod = ApiMethod.HttpMethod.POST )
    public DataSetUpgrade insertUpgrade( DataSetUpgrade upgrade, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );
        log.info( "DataSetUpgrade: " + upgrade );

        Long dataSetId = upgrade.getDataSetId();

        upgrade.setCompletedAt( null );
        upgrade.setCompleted( false );
        upgrade.setAuthorized( authUser == null ? null : authUser.getEmail() );

        ofy().save().entity( upgrade ).now();

        processor.upgrade( dataSetId, upgrade.getId() );

        return upgrade;
    }

    @ApiMethod( name = "dataset.upgrade.remove", path = "dataset.upgrade/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void removeUpgrade( @Named( "id" ) Long id, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );
        log.info( "remove: " + id );
    }

    @ApiMethod( name = "dataset.export.get", path = "dataset.export/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public DataSetExport getExport( @Named( "id" ) Long id, com.google.appengine.api.users.User authUser )
    {
        DataSetExport upgrade = new DataSetExport( id );

        upgrade.setCompletedAt( new Date() );
        upgrade.setCompleted( true );
        upgrade.setDataSetId( 1L );

        log.info( "User: " + authUser );

        return upgrade;
    }

    @ApiMethod( name = "dataset.export.insert", path = "dataset.export", httpMethod = ApiMethod.HttpMethod.POST )
    public DataSetExport insertExport( DataSetExport export, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );
        log.info( "DataSetUpgrade: " + export );

        return export;
    }

    @ApiMethod( name = "dataset.export.remove", path = "dataset.export/{id}", httpMethod = ApiMethod.HttpMethod.DELETE )
    public void removeExport( @Named( "id" ) Long id, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );
        log.info( "remove: " + id );
    }
}
