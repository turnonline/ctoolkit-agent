package org.ctoolkit.migration.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import org.ctoolkit.migration.agent.model.KindMetaData;
import org.ctoolkit.migration.agent.model.PropertyMetaData;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.List;

/**
 * Endpoint for metadata info - entity kinds, entity properties
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
public class MetadataEndpoint
{
    private final ChangeSetService service;

    @Inject
    public MetadataEndpoint( ChangeSetService service )
    {
        this.service = service;
    }

    @ApiMethod( name = "metadata.kind.list", path = "metadata/kind", httpMethod = ApiMethod.HttpMethod.GET )
    public List<KindMetaData> listKinds( User authUser ) throws Exception
    {
        return service.kinds();
    }

    @ApiMethod( name = "metadata.kind.property.list", path = "metadata/{kind}/property", httpMethod = ApiMethod.HttpMethod.GET )
    public List<PropertyMetaData> listProperties( @Named( "kind" ) String kind, User authUser ) throws Exception
    {
        return service.properties( kind );
    }
}
