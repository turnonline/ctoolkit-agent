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
import com.google.api.server.spi.config.ApiNamespace;

/**
 * The endpoint base class as a configuration of the REST API and generated client.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api( name = "agent",
        canonicalName = "Ctoolkit Agent",
        title = "Cloud Toolkit Migration Agent",
        version = "v1",
        description = "REST API for Cloud Toolkit Migration Agent",
        documentationLink = "https://c-toolkit.appspot.com/docs",
        authenticators = {IAMAuthenticator.class},
        namespace = @ApiNamespace( ownerDomain = "api.ctoolkit.org", ownerName = "Comvai, s.r.o." )
)
public class AgentEndpointConfig
{
}
