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

import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Endpoint for metadata info - entity kinds, entity properties
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Path( "/" )
public class MetadataEndpoint
{
    private ChangeSetService service;

    public MetadataEndpoint()
    {
    }

    @Inject
    public MetadataEndpoint( ChangeSetService service )
    {
        this.service = service;
    }

    @GET
    @Path( "/metadata/kind" )
    @Produces( "application/json" )
    public List<KindMetaData> listKinds() throws Exception
    {
        return service.kinds();
    }

    @GET
    @Path( "/metadata/{kind}/property" )
    @Produces( "application/json" )
    public List<PropertyMetaData> listProperties( @PathParam( "kind" ) String kind ) throws Exception
    {
        return service.properties( kind );
    }
}
