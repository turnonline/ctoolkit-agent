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

import biz.turnonline.ecosystem.model.api.MigrationSetEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetGroovyEnricher;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Implementation of {@link MigrationSetEnricher} enricher
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class GroovyEnricherProcessor
        implements EnricherProcessor<MigrationSetGroovyEnricher>
{
    @Override
    public void enrich( MigrationSetGroovyEnricher enricher, Map<String, Object> ctx )
    {
        Binding binding = new Binding( ctx );
        binding.setVariable( "ctx", ctx );

        GroovyShell shell = new GroovyShell( binding );
        shell.evaluate( enricher.getCommand() );
    }
}
