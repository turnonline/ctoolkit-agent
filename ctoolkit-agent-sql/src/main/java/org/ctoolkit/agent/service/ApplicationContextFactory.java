/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;

/**
 * Application context factory for sql workers
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ApplicationContextFactory
{
    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( ImportPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driverClassName", pipelineOptions.getJdbcDriver()
        ) );

        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }

    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( MigrationPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driverClassName", pipelineOptions.getJdbcDriver()
        ) );

        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }
}
