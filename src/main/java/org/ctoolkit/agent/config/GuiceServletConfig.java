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

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Application guice module.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class GuiceServletConfig
        extends GuiceServletContextListener
{
    private Injector getTestInjector()
    {
        return Guice.createInjector( Stage.PRODUCTION, // TODO: remove
                new AgentModule(),
                new AgentServletModule(),
                new IAMModule(),
                new StorageModule()
        );
    }

    private Injector getProductionInjector()
    {
        return Guice.createInjector( Stage.PRODUCTION,
                new AgentModule(),
                new AgentServletModule(),
                new IAMModule(),
                new StorageModule()
        );
    }

    @Override
    protected final Injector getInjector()
    {
        if ( SystemProperty.environment.value() == SystemProperty.Environment.Value.Production )
        {
            // The app is running on App Engine...
            return getProductionInjector();
        }
        else
        {
            return getTestInjector();
        }
    }
}
