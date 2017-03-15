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

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Authorization interceptor checks if method contains parameter {@link User} and is not <code>null</code>.
 * If user is not found in method signature or is <code>null</code>, interceptor will throw {@link UnauthorizedException}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuthorizationInterceptor
        implements MethodInterceptor
{
    private Logger log = LoggerFactory.getLogger( AuthorizationInterceptor.class );

    @Override
    public Object invoke( MethodInvocation invocation ) throws Throwable
    {
        int index = -1;
        Type[] types = invocation.getMethod().getGenericParameterTypes();

        for ( int i = 0; i < types.length; i++ )
        {
            if ( types[i].equals( User.class ) )
            {
                index = i;
            }
        }

        if ( index == -1 )
        {
            log.warn( "No '" + User.class.getName() + "' is present in method signature. This is probably a bug." );
            throw new UnauthorizedException( "User is not authorized." );
        }

        if ( invocation.getArguments()[index] == null )
        {
            throw new UnauthorizedException( "User is not authorized." );
        }

        return invocation.proceed();
    }
}
