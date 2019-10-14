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

package biz.turnonline.ecosystem.service;

import biz.turnonline.ecosystem.service.beam.options.JdbcPipelineOptions;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import org.apache.beam.sdk.options.PipelineOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Application context factory for concrete agents
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ApplicationContextFactory
{
    @SuppressWarnings( "unchecked" )
    public static ApplicationContext create( PipelineOptions pipelineOptions )
    {
        Map<String, Object> properties = new HashMap<>();

        if ( pipelineOptions instanceof JdbcPipelineOptions )
        {
            JdbcPipelineOptions jdbcPipelineOptions = ( JdbcPipelineOptions ) pipelineOptions;

            properties.putAll( CollectionUtils.mapOf(
                    "datasources.default.url", jdbcPipelineOptions.getJdbcUrl(),
                    "datasources.default.username", jdbcPipelineOptions.getJdbcUsername(),
                    "datasources.default.password", jdbcPipelineOptions.getJdbcPassword(),
                    "datasources.default.driverClassName", jdbcPipelineOptions.getJdbcDriver()
            ) );
        }

        ApplicationContext ctx = ApplicationContext.run( properties );
        ctx.registerSingleton( pipelineOptions, true );

        return ctx;
    }
}
