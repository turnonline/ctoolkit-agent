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

package org.ctoolkit.agent.config;

import com.google.inject.servlet.ServletModule;
import org.ctoolkit.agent.AccessControlAllowOriginFilter;
import org.ctoolkit.agent.UploadJsonCredentialsServlet;
import org.ctoolkit.agent.rest.AgentApplication;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentServletModule
        extends ServletModule
{
    private static final String ENDPOINTS_SERVLET_PATH = "/_ah/api/agent/v1/*";

    @Override
    protected void configureServlets()
    {
        // setup jersey
        Map<String, String> params = new HashMap<>();
        params.put( ServletProperties.JAXRS_APPLICATION_CLASS, AgentApplication.class.getName() );

        bind( ServletContainer.class ).in( Singleton.class );
        serve( ENDPOINTS_SERVLET_PATH ).with( ServletContainer.class, params );

        // access control filter
        bind( AccessControlAllowOriginFilter.class ).in( Singleton.class );
        filter( "/*" ).through( AccessControlAllowOriginFilter.class );

        // upload json credentials servlet
        bind( UploadJsonCredentialsServlet.class ).in( Singleton.class );
        serve( "/upload-json-credentials" ).with( UploadJsonCredentialsServlet.class );

        // TODO: fix appstats - probably does not work becaues it is defined here and also in web.xml
        // appstats configuration
//        bind( AppstatsServlet.class ).in( Singleton.class );
//        bind( AppstatsFilter.class ).in( Singleton.class );

//        serve( "/appstats/*" ).with( AppstatsServlet.class );

        // Appstats configuration
//        Map<String, String> initParams = new HashMap<String, String>();
//        initParams.put( "logMessage", "Appstats available: /appstats/details?time={ID}" );
//
//        // exclude appstats itself from logging
//        filterRegex( "^((?!/appstats/).)*$" ).through( AppstatsFilter.class, initParams );
    }
}
