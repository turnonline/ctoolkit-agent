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
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import java.util.List;

/**
 * Endpoint for metadata info - entity kinds, entity properties
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Api
@ApiReference( AgentEndpointConfig.class )
@Authorized
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
