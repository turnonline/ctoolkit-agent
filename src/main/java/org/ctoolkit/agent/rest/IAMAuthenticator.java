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

import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsRequest;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsResponse;
import com.google.appengine.api.utils.SystemProperty;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.restapi.client.identity.Identity;
import org.ctoolkit.services.identity.IdentityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

/**
 * IAM authenticator. First it checks if header contains valid JWT token and
 * than for production checks if requested user has permission 'datastore.entities.create' set.
 * It also retrieves JWT token attributes like email and puts it into {@link RestContext}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Priority( Priorities.AUTHENTICATION )
@javax.ws.rs.ext.Provider
public class IAMAuthenticator
        implements ContainerRequestFilter
{
    private static final String X_CTOOLKIT_AGENT_ON_BEHALF_OF_AGENT_URL = "-X-CtoolkitAgent-onBehalfOfAgentUrl";

    private static final String PERSMISSION = "datastore.entities.create";

    private Logger log = LoggerFactory.getLogger( IAMAuthenticator.class );

    private IdentityHandler identityHandler;

    private Provider<RestContext> restContextProvider;

    private CloudResourceManager cloudResourceManager;

    private HttpServletRequest request;

    public IAMAuthenticator()
    {
    }

    @Inject
    public IAMAuthenticator( IdentityHandler identityHandler,
                             Provider<RestContext> restContextProvider,
                             CloudResourceManager cloudResourceManager,
                             @Context HttpServletRequest request )
    {
        this.identityHandler = identityHandler;
        this.restContextProvider = restContextProvider;
        this.cloudResourceManager = cloudResourceManager;
        this.request = request;
    }

    @Override
    public void filter( ContainerRequestContext requestContext ) throws IOException
    {
        authenticate( request, requestContext );
    }

    private void authenticate( HttpServletRequest request, ContainerRequestContext requestContext )
    {
        try
        {
            Identity identity = identityHandler.resolve( request );

            if ( identity != null )
            {
                String email = identity.getEmail();

                RestContext ctx = restContextProvider.get();

                // store user info to ThreadLocal context - will be used in auditing
                ctx.setUserId( identity.getLocalId() );
                ctx.setUserEmail( email );
                ctx.setDisplayName( identity.getDisplayName() );
                ctx.setPhotoUrl( identity.getPhotoUrl() );
                ctx.setGtoken( identityHandler.getToken( request ) );
                ctx.setOnBehalfOfAgentUrl( request.getHeader( X_CTOOLKIT_AGENT_ON_BEHALF_OF_AGENT_URL ) );

                // if agent is running on app engine - check permissions
                if ( SystemProperty.environment.value() == SystemProperty.Environment.Value.Production )
                {
                    String resource = SystemProperty.applicationId.get();
                    TestIamPermissionsRequest content = new TestIamPermissionsRequest();
                    content.setPermissions( new ArrayList<String>() );
                    content.getPermissions().add( PERSMISSION );

                    CloudResourceManager.Projects.TestIamPermissions testRequest = cloudResourceManager
                            .projects()
                            .testIamPermissions( resource, content );

                    TestIamPermissionsResponse response = testRequest.execute();
                    if ( !response.getPermissions().isEmpty() ) // if user has permission API returns same list as requested
                    {
                        return;
                    }
                    else
                    {
                        log.error( "User '" + email + "' does not have permission '" + PERSMISSION + "'. " +
                                "Please add this permission via console: 'https://console.cloud.google.com/iam-admin/iam/project'" );
                    }
                }
                // if agent is running in localhost - skip permission check
                else
                {
                    return;
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "Unable to verify token", e );
        }

        requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
    }
}
