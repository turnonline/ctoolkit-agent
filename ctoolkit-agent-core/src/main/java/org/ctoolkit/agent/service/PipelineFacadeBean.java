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

import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.beam.PipelineOptionsFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of {@link PipelineFacade}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class PipelineFacadeBean
        implements PipelineFacade
{
    @Inject
    private PipelineOptionsFactory pipelineOptionsFactory;

    @Inject
    private MigrationBeamPipeline migrationPipeline;

    @Inject
    private ImportBeamPipeline importPipeline;

    @Inject
    @Nullable
    private MigrationPipelineOptions migrationPipelineOptions;

    @Override
    public PipelineOptionsFactory pipelineOptionsFactory()
    {
        return pipelineOptionsFactory;
    }

    @Override
    public MigrationBeamPipeline migrationPipeline()
    {
        return migrationPipeline;
    }

    @Override
    public ImportBeamPipeline importPipeline()
    {
        return importPipeline;
    }

    @Override
    public MigrationPipelineOptions migrationPipelineOptions()
    {
        return migrationPipelineOptions;
    }
}
