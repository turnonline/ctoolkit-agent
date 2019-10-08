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

import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;

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
