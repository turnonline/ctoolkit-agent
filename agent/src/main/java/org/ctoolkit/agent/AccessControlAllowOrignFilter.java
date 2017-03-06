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

package org.ctoolkit.agent;

import com.google.api.client.http.HttpMethods;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter adds Access-Control-Allow-* response headers + handles options method wich is called before an actual call
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AccessControlAllowOrignFilter
        implements Filter
{
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        // noop
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException, ServletException
    {
        HttpServletResponse httpResponse = ( HttpServletResponse ) response;
        HttpServletRequest httpRequest = ( HttpServletRequest ) request;

        httpResponse.setHeader( "Access-Control-Allow-Origin", "*" );
        httpResponse.setHeader( "Access-Control-Allow-Methods", "GET,PUT,POST,DELETE" );
        httpResponse.setHeader( "Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Content-Type,gtoken,-X-CtoolkitAgent-onBehalfOfAgentUrl" );

        if ( !httpRequest.getMethod().equals( HttpMethods.OPTIONS ) )
        {
            chain.doFilter( request, response );
        }
    }

    @Override
    public void destroy()
    {
        // noop
    }
}
