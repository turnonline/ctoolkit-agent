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

package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Abstract class for beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class BeamPipeline<BATCH, OPTIONS extends PipelineOptions>
        implements Serializable
{
    /**
     * Create pipeline
     *
     * @param batch   link batch type  - {@link MigrationBatch} or {@link ImportBatch}
     * @param options pipeline options - {@link MigrationPipelineOptions} or {@link ImportPipelineOptions}
     * @return @link Pipeline
     */
    public abstract Pipeline create( BATCH batch, OPTIONS options );

    protected Logger log()
    {
        return LoggerFactory.getLogger( getClass() );
    }
}
