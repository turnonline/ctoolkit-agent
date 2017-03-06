/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.api.users.User;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.MetadataAudit.Operation;
import org.ctoolkit.agent.service.ChangeSetService;

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
