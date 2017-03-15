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

import com.google.common.io.CharStreams;
import org.ctoolkit.services.common.PropertyService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;

import static org.ctoolkit.agent.config.AgentModule.CONFIG_JSON_CREDENTIALS;

/**
 * Servlet is used to upload json credentials file
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class UploadJsonCredentialsServlet extends HttpServlet
{
    private static final long serialVersionUID = 2516420453149547193L;

    private final PropertyService propertyService;

    @Inject
    public UploadJsonCredentialsServlet( PropertyService propertyService )
    {
        this.propertyService = propertyService;
    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        String string = CharStreams.toString( new InputStreamReader( req.getInputStream(), "UTF-8" ) );
        String json = URLDecoder.decode( string.replace( "json=","" ) );

        propertyService.setString( CONFIG_JSON_CREDENTIALS, json );

        resp.getWriter().append( "JSON credentials uploaded successfully!" );
    }

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        PrintWriter writer = resp.getWriter();
        writer.append( "<form action='/upload-json-credentials' method='POST'>" );
        writer.append( "<textarea style='display:block;width:500px;height:300px;margin-bottom:10px;' name='json' placeholder='JSON credentials'></textarea>" );
        writer.append( "<button type='submit'>Upload</button>" );
        writer.append( "</form>" );

        resp.setContentType( "text/html" );
    }
}
