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
import com.google.api.control.extensions.appengine.GoogleAppEngineControlFilter;
import com.google.api.server.spi.ServletInitializationParameters;
import com.google.api.server.spi.guice.EndpointsModule;
import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.appengine.tools.mapreduce.MapReduceServlet;
import com.google.appengine.tools.pipeline.impl.servlets.PipelineServlet;
import com.googlecode.objectify.ObjectifyFilter;
import org.ctoolkit.agent.AccessControlAllowOrignFilter;
import org.ctoolkit.agent.UploadJsonCredentialsServlet;
import org.ctoolkit.agent.rest.AuditEndpoint;
import org.ctoolkit.agent.rest.ChangeEndpoint;
import org.ctoolkit.agent.rest.ExportEndpoint;
import org.ctoolkit.agent.rest.ImportEndpoint;
import org.ctoolkit.agent.rest.MetadataEndpoint;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentServletModule
        extends EndpointsModule
{
    private static final String ENDPOINTS_SERVLET_PATH = "/_ah/api/*";

    private static final String PROJECT_ID = "c-toolkit";

    @Override
    protected void configureServlets()
    {
        // endpoints filter
        ServletInitializationParameters params = ServletInitializationParameters.builder()
                .addServiceClass( ImportEndpoint.class )
                .addServiceClass( ChangeEndpoint.class )
                .addServiceClass( ExportEndpoint.class )
                .addServiceClass( MetadataEndpoint.class )
                .addServiceClass( AuditEndpoint.class )
                .setRestricted( false )
                // this is important, otherwise we cannot use certificates from third-party applications
                .setClientIdWhitelistEnabled( false ).build();

        configureEndpoints( ENDPOINTS_SERVLET_PATH, params );

        bind( ServiceManagementConfigFilter.class ).in( Singleton.class );
        filter( ENDPOINTS_SERVLET_PATH ).through( ServiceManagementConfigFilter.class );

        Map<String, String> apiController = new HashMap<>();
        apiController.put( "endpoints.projectId", PROJECT_ID );
        apiController.put( "endpoints.serviceName", "agent.endpoints." + PROJECT_ID + ".cloud.goog" );

        bind( GoogleAppEngineControlFilter.class ).in( Singleton.class );
        filter( ENDPOINTS_SERVLET_PATH ).through( GoogleAppEngineControlFilter.class, apiController );

        // objectify filter
        bind( ObjectifyFilter.class ).in( Singleton.class );
        filter( "/*" ).through( ObjectifyFilter.class );

        // access control filter
        bind( AccessControlAllowOrignFilter.class ).in( Singleton.class );
        filter( "/*" ).through( AccessControlAllowOrignFilter.class );

        // map reduce servlets
        bind( MapReduceServlet.class ).in( Singleton.class );
        serve( "/mapreduce/*" ).with( MapReduceServlet.class );

        bind( PipelineServlet.class ).in( Singleton.class );
        serve( "/_ah/pipeline/*" ).with( PipelineServlet.class );

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
