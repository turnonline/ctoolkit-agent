package org.ctoolkit.migration.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.api.users.User;
import org.ctoolkit.migration.agent.model.AuditFilter;
import org.ctoolkit.migration.agent.model.MetadataAudit;
import org.ctoolkit.migration.agent.model.MetadataAudit.Operation;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.List;

/**
 * Endpoint for audit
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
public class AuditEndpoint
{
    private final ChangeSetService service;

    @Inject
    public AuditEndpoint( ChangeSetService service )
    {
        this.service = service;
    }

    @ApiMethod( name = "audit.list", path = "audit", httpMethod = ApiMethod.HttpMethod.GET )
    public List<MetadataAudit> listAudit( @DefaultValue( "0" ) @Nullable @Named( "start" ) Integer start,
                                          @DefaultValue( "10" ) @Nullable @Named( "length" ) Integer length,
                                          @DefaultValue( "createDate" ) @Nullable @Named( "orderBy" ) String orderBy,
                                          @DefaultValue( "false" ) @Nullable @Named( "ascending" ) Boolean ascending,
                                          @Nullable @Named( "operation" ) Operation operation,
                                          @Nullable @Named( "ownerId" ) String ownerId,
                                          User authUser ) throws Exception
    {
        AuditFilter filter = new AuditFilter.Builder()
                .setStart( start )
                .setLength( length )
                .setOrderBy( orderBy )
                .setAscending( ascending )
                .setOperation( operation )
                .setOwnerId( ownerId )
                .build();

        return service.list( filter );
    }
}
