package org.ctoolkit.agent.restapi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import org.ctoolkit.agent.restapi.resource.EntitySchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * The entity schema REST API endpoint.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
public class EntitySchemaEndpoint
{
    private static final Logger log = LoggerFactory.getLogger( EntitySchemaEndpoint.class );

    @ApiMethod( name = "entityschema.get", path = "entityschema/{kind}", httpMethod = ApiMethod.HttpMethod.GET )
    public EntitySchema getEntitySchema( @Named( "kind" ) String kind, com.google.appengine.api.users.User authUser )
    {
        EntitySchema diff = new EntitySchema();

        diff.setEntityKind( kind );

        log.info( "User: " + authUser );
        log.info( "Kind: " + kind );

        return diff;
    }

    @ApiMethod( name = "entityschema.list", path = "entityschema", httpMethod = ApiMethod.HttpMethod.GET )
    public List<EntitySchema> getEntitySchemas( @Nullable @Named( "entityKindStart" ) String entityKindStart,
                                                com.google.appengine.api.users.User authUser )
    {
        EntitySchema schema = new EntitySchema();

        log.info( "User: " + authUser );

        List<EntitySchema> list = new ArrayList<>();
        list.add( schema );

        return list;
    }
}
