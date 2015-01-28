package org.ctoolkit.agent.restapi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import org.ctoolkit.agent.restapi.resource.DiffSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

/**
 * The diff set REST API endpoint.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
public class DiffSetEndpoint
{
    private static final Logger log = LoggerFactory.getLogger( DiffSetEndpoint.class );

    @ApiMethod( name = "diffset.get", path = "diffset/{id}", httpMethod = ApiMethod.HttpMethod.GET )
    public DiffSet getDiffSet( @Named( "id" ) Long id, com.google.appengine.api.users.User authUser )
    {
        DiffSet diffSet = new DiffSet( id );

        log.info( "User: " + authUser );
        log.info( "Id: " + id );

        return diffSet;
    }

    @ApiMethod( name = "diffset.insert", path = "diffset", httpMethod = ApiMethod.HttpMethod.POST )
    public DiffSet insertDiffSet( DiffSet diff, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );

        return diff;
    }

    @ApiMethod( name = "diffset.update", path = "diffset/{id}", httpMethod = ApiMethod.HttpMethod.PUT )
    public void updateDiffSet( DiffSet diff, com.google.appengine.api.users.User authUser )
    {
        log.info( "User: " + authUser );
    }
}
