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

import biz.turnonline.ecosystem.model.api.ImportSet;
import biz.turnonline.ecosystem.service.ApplicationContextFactory;
import biz.turnonline.ecosystem.service.ImportService;
import biz.turnonline.ecosystem.service.beam.options.ImportPipelineOptions;
import io.micronaut.context.ApplicationContext;
import org.apache.beam.sdk.transforms.DoFn;

/**
 * Do function for importing of {@link ImportSet}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ImportDoFn
        extends DoFn<ImportSet, Void>
{
    @ProcessElement
    public void processElement( ProcessContext c )
    {
        ImportPipelineOptions pipelineOptions = c.getPipelineOptions().as( ImportPipelineOptions.class );
        ApplicationContext ctx = ApplicationContextFactory.create( pipelineOptions );
        ImportService service = ctx.getBean( ImportService.class );

        service.importData( c.element() );
    }
}
