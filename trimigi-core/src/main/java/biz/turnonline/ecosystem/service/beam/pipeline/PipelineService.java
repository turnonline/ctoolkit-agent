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

package biz.turnonline.ecosystem.service.beam.pipeline;

import biz.turnonline.ecosystem.model.Export;
import biz.turnonline.ecosystem.model.api.ImportBatch;
import biz.turnonline.ecosystem.model.api.ImportJob;
import biz.turnonline.ecosystem.model.api.ImportSet;
import biz.turnonline.ecosystem.model.api.MigrationBatch;
import biz.turnonline.ecosystem.model.api.MigrationJob;
import biz.turnonline.ecosystem.model.api.MigrationSet;

import java.util.List;

/**
 * Public migration service API
 *
 * @author <a href="mailto:pohorelec@turnonline.biz">Jozef Pohorelec</a>
 */
public interface PipelineService
{
    /**
     * Migrate batch of migration sets
     *
     * @param batch {@link MigrationBatch}
     * @return {@link MigrationJob}
     */
    MigrationJob migrateBatch( MigrationBatch batch );

    /**
     * Import batch of import sets
     *
     * @param batch {@link ImportBatch}
     * @return {@link ImportJob}
     */
    ImportJob importBatch( ImportBatch batch );

    /**
     * Transform list of entities to list of {@link ImportSet} using {@link MigrationSet} rules
     *
     * @param migrationSet       {@link MigrationSet}
     * @param exportList list of {@link Export}
     * @return {@link ImportSet}
     */
    List<ImportSet> transform( MigrationSet migrationSet, List<Export> exportList );

    /**
     * Import list {@link ImportSet} to target agent
     *
     * @param importSets {@link ImportSet}
     */
    void importToTargetAgent( List<ImportSet> importSets );
}
