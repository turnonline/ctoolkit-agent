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

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsRequest;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsResponse;
import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Injector;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.restapi.client.identity.Identity;
import org.ctoolkit.services.common.PropertyService;
import org.ctoolkit.services.identity.IdentityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class IAMAuthenticator
        implements Authenticator
{
    private static final String X_CTOOLKIT_AGENT_ON_BEHALF_ON_AGENT_URL = "-X-CtoolkitAgent-onBehalfOfAgentUrl";

    private Logger log = LoggerFactory.getLogger( IAMAuthenticator.class );

    @Inject
    private static Injector injector;

    @Inject
    private IdentityHandler identityHandler;

    @Inject
    private RestContext ctx;

    @Inject
    private CloudResourceManager cloudResourceManager;

    @Inject
    private PropertyService common;

    @Override
    public User authenticate( HttpServletRequest request )
    {
        injector.injectMembers( this );

        try
        {
            Identity identity = identityHandler.resolve( request );

            if ( identity != null )
            {
                String email = identity.getEmail();

                // store user info to ThreadLocal context - will be used in auditing
                ctx.setUserId( identity.getLocalId() );
                ctx.setUserEmail( email );
                ctx.setDisplayName( identity.getDisplayName() );
                ctx.setPhotoUrl( identity.getPhotoUrl() );
                ctx.setGtoken( identityHandler.getToken( request ) );
                ctx.setOnBehalfOfAgentUrl( request.getHeader( X_CTOOLKIT_AGENT_ON_BEHALF_ON_AGENT_URL ) );

                // if agent is running on app engine - check permissions
                if ( !common.isDevelopmentEnvironment() )
                {
                    String resource = SystemProperty.applicationId.get();
                    TestIamPermissionsRequest content = new TestIamPermissionsRequest();
                    content.setPermissions( new ArrayList<String>() );
                    content.getPermissions().add( "datastore.entities.create" ); // TODO: is this permission sufficient?

                    CloudResourceManager.Projects.TestIamPermissions testRequest = cloudResourceManager
                            .projects()
                            .testIamPermissions( resource, content );

                    TestIamPermissionsResponse response = testRequest.execute();
                    if ( !response.getPermissions().isEmpty() ) // if user has permission API returns same list as requested
                    {
                        return new User( email );
                    }
                }
                // if agent is running in in localhost - skip permission check
                else
                {
                    return new User( email );
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "Unable to verify token", e );
        }

        return null;
    }
}
