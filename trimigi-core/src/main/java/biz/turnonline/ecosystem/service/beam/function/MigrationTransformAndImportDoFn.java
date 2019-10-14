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

package biz.turnonline.ecosystem.service.beam.function;

import biz.turnonline.ecosystem.model.Export;
import biz.turnonline.ecosystem.model.api.ImportBatch;
import biz.turnonline.ecosystem.model.api.ImportSet;
import biz.turnonline.ecosystem.model.api.MigrationSet;
import biz.turnonline.ecosystem.service.ApplicationContextFactory;
import biz.turnonline.ecosystem.service.beam.options.MigrationPipelineOptions;
import biz.turnonline.ecosystem.service.beam.pipeline.PipelineService;
import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.util.List;

/**
 * Do function for transform list of {@link Export} to {@link ImportBatch} using {@link MigrationSet}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MigrationTransformAndImportDoFn
        extends DoFn<KV<MigrationSet, List<Export>>, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        MigrationPipelineOptions pipelineOptions = c.getPipelineOptions().as( MigrationPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        PipelineService service = ctx.getBean( PipelineService.class );

        // transform to import sets
        List<ImportSet> importSets = service.transform( c.element().getKey(), c.element().getValue() );

        // import import sets into target agent
        service.importToTargetAgent( importSets );
    }
}
