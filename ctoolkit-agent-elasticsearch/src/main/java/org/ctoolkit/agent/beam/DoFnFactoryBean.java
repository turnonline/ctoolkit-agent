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

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.io.Serializable;
import java.util.List;

/**
 * Implementation of {@link DoFnFactory}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class DoFnFactoryBean
        implements DoFnFactory, Serializable
{
    @Bean
    @Override
    public DoFn<ImportSet, Void> createImportDoFn()
    {
        return new ImportDoFn();
    }

    @Bean
    @Override
    public DoFn<MigrationSet, KV<MigrationSet, String>> createSplitQueriesDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, String>, KV<MigrationSet, List<EntityExportData>>> createRetrieveEntityMetadataListDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, List<EntityExportData>>, Void> createTransformAndImportDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }
}
