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

import ma.glasnost.orika.MapperFacade;
import org.ctoolkit.agent.model.Audit;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.Operation;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint for audit
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Path( "/" )
public class AuditEndpoint
{
    private ChangeSetService service;

    private MapperFacade mapper;

    public AuditEndpoint()
    {
    }

    @Inject
    public AuditEndpoint( ChangeSetService service, MapperFacade mapper )
    {
        this.service = service;
        this.mapper = mapper;
    }

    @GET
    @Path( "/audit" )
    @Produces( "application/json" )
    public List<Audit> listAudit( @DefaultValue( "0" ) @QueryParam( "start" ) Integer start,
                                  @DefaultValue( "10" ) @QueryParam( "length" ) Integer length,
                                  @DefaultValue( "createDate" ) @QueryParam( "orderBy" ) String orderBy,
                                  @DefaultValue( "false" ) @QueryParam( "ascending" ) Boolean ascending,
                                  @QueryParam( "operation" ) Operation operation,
                                  @QueryParam( "ownerId" ) String ownerId )
    {
        AuditFilter filter = new AuditFilter.Builder()
                .setStart( start )
                .setLength( length )
                .setOrderBy( orderBy )
                .setAscending( ascending )
                .setOperation( operation )
                .setOwnerId( ownerId )
                .build();

        List<MetadataAudit> list = service.list( filter );
        List<Audit> auditList = new ArrayList<>();
        for ( MetadataAudit metadataAudit : list )
        {
            auditList.add( mapper.map( metadataAudit, Audit.class ) );
        }

        return auditList;
    }
}
