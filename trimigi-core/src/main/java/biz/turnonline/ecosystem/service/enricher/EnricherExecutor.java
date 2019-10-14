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

package biz.turnonline.ecosystem.service.enricher;

import biz.turnonline.ecosystem.model.api.MigrationSetDatabaseSelectEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetGroovyEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetRestEnricher;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Enricher executor is used to apply {@link MigrationSetEnricher} via {@link EnricherProcessor}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class EnricherExecutor
{
    private Map<Class, EnricherProcessor> enricherProcessors = new HashMap<>();

    @Inject
    private RestEnricherProcessor restEnricherProcessor;

    @Inject
    private DatabaseEnricherProcessor databaseEnricherProcessor;

    @Inject
    private GroovyEnricherProcessor groovyEnricherProcessor;

    @SuppressWarnings( "unchecked" )
    public void enrich( MigrationSetEnricher enricher, Map<String, Object> ctx )
    {
        enricherProcessors.get( enricher.getClass() ).enrich( enricher, ctx );
    }

    @PostConstruct
    public void init()
    {
        enricherProcessors.put( MigrationSetRestEnricher.class, restEnricherProcessor );
        enricherProcessors.put( MigrationSetDatabaseSelectEnricher.class, databaseEnricherProcessor );
        enricherProcessors.put( MigrationSetGroovyEnricher.class, groovyEnricherProcessor );
    }
}
