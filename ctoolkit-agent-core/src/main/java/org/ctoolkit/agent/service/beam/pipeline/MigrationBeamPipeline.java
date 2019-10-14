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

package org.ctoolkit.agent.service.beam.pipeline;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.beam.function.MigrationRetrieveContextList;
import org.ctoolkit.agent.service.beam.function.MigrationSplitQueriesDoFn;
import org.ctoolkit.agent.service.beam.function.MigrationTransformAndImportDoFn;
import org.ctoolkit.agent.service.beam.options.MigrationPipelineOptions;

import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Migration beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationBeamPipeline
        extends BeamPipeline<MigrationBatch, MigrationPipelineOptions>
{
    @Override
    public Pipeline create( final MigrationBatch batch, MigrationPipelineOptions options )
    {
        Pipeline pipeline = Pipeline.create( options );
        pipeline
                .apply( "Split migration sets", new PTransform<PBegin, PCollection<MigrationSet>>()
                {
                    @Override
                    public PCollection<MigrationSet> expand( PBegin input )
                    {
                        List<MigrationSet> migrationSets = batch.getMigrationSets();
                        Create.Values<MigrationSet> values;
                        if ( migrationSets.isEmpty() )
                        {
                            values = Create.empty( new TypeDescriptor<MigrationSet>() {} );
                        }
                        else
                        {
                            values = Create.of( migrationSets );
                        }

                        return input.apply( values );
                    }
                } )
                .apply( "Split queries", ParDo.of( new MigrationSplitQueriesDoFn() ) )
                .apply( "Retrieve context", ParDo.of( new MigrationRetrieveContextList() ) )
                .apply( "Transform to ImportSet and send to target agent", ParDo.of( new MigrationTransformAndImportDoFn() ) );

        return pipeline;
    }

    private static class MigrationSetListType
            implements Type
    {
    }
}
