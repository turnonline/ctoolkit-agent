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

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.ctoolkit.agent.converter.BaseConverterRegistrat;
import org.ctoolkit.agent.converter.ConverterRegistrat;
import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.converter.MongoConverterRegistrat;
import org.ctoolkit.agent.model.Agent;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter registrat factory provides map of {@link Agent} to {@link ConverterRegistrat} mapping
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class RegistratFactory
{
    @Bean
    @Singleton
    public Map<Agent, BaseConverterRegistrat> provideRegistrats(
            ElasticsearchConverterRegistrat elasticsearchConverterRegistrat,
            MongoConverterRegistrat mongoConverterRegistrat
    )
    {
        Map<Agent, BaseConverterRegistrat> registrats = new HashMap<>(  );
        registrats.put( Agent.ELASTICSEARCH, elasticsearchConverterRegistrat );
        registrats.put( Agent.MONGO, mongoConverterRegistrat );
        return registrats;
    }
}
