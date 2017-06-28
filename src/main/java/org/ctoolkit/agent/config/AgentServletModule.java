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

import com.google.api.control.ServiceManagementConfigFilter;
import com.google.api.server.spi.ServletInitializationParameters;
import com.google.api.server.spi.guice.EndpointsModule;
import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.googlecode.objectify.ObjectifyFilter;
import org.ctoolkit.agent.AccessControlAllowOrignFilter;
import org.ctoolkit.agent.UploadJsonCredentialsServlet;
import org.ctoolkit.agent.rest.AuditEndpoint;
import org.ctoolkit.agent.rest.ExportEndpoint;
import org.ctoolkit.agent.rest.ImportEndpoint;
import org.ctoolkit.agent.rest.MetadataEndpoint;
import org.ctoolkit.agent.rest.MigrationEndpoint;

import javax.inject.Singleton;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentServletModule
        extends EndpointsModule
{
    private static final String ENDPOINTS_SERVLET_PATH = "/_ah/api/*";

    @Override
    protected void configureServlets()
    {
        // endpoints filter
        ServletInitializationParameters params = ServletInitializationParameters.builder()
                .addServiceClass( ImportEndpoint.class )
                .addServiceClass( ExportEndpoint.class )
                .addServiceClass( MigrationEndpoint.class )
                .addServiceClass( MetadataEndpoint.class )
                .addServiceClass( AuditEndpoint.class )
                .setRestricted( false )
                // this is important, otherwise we cannot use certificates from third-party applications
                .setClientIdWhitelistEnabled( false ).build();

        configureEndpoints( ENDPOINTS_SERVLET_PATH, params );

        bind( ServiceManagementConfigFilter.class ).in( Singleton.class );
        filter( ENDPOINTS_SERVLET_PATH ).through( ServiceManagementConfigFilter.class );

        /* TODO: not working for flexible environment
        String projectId = SystemProperty.applicationId.get();
        Map<String, String> apiController = new HashMap<>();
        apiController.put( "endpoints.projectId", projectId );
        apiController.put( "endpoints.serviceName", "agent.endpoints." + projectId + ".cloud.goog" );

        bind( GoogleAppEngineControlFilter.class ).in( Singleton.class );
        filter( ENDPOINTS_SERVLET_PATH ).through( GoogleAppEngineControlFilter.class, apiController );
        */

        // objectify filter
        bind( ObjectifyFilter.class ).in( Singleton.class );
        filter( "/*" ).through( ObjectifyFilter.class );

        // access control filter
        bind( AccessControlAllowOrignFilter.class ).in( Singleton.class );
        filter( "/*" ).through( AccessControlAllowOrignFilter.class );

        // upload json credentials servlet
        bind( UploadJsonCredentialsServlet.class ).in( Singleton.class );
        serve( "/upload-json-credentials" ).with( UploadJsonCredentialsServlet.class );

        // appstats configuration
        bind( AppstatsServlet.class ).in( Singleton.class );
        bind( AppstatsFilter.class ).in( Singleton.class );

        serve( "/appstats/*" ).with( AppstatsServlet.class );

        // Appstats configuration
//        Map<String, String> initParams = new HashMap<String, String>();
//        initParams.put( "logMessage", "Appstats available: /appstats/details?time={ID}" );
//
//        // exclude appstats itself from logging
//        filterRegex( "^((?!/appstats/).)*$" ).through( AppstatsFilter.class, initParams );
    }
}
