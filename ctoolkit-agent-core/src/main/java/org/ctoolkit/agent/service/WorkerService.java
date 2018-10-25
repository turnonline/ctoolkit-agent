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

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.util.List;

/**
 * Worker service as a support for beams {@link org.apache.beam.sdk.transforms.DoFn} functions
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface WorkerService
{
    /**
     * Split root query to offset queries.
     *
     * @param migrationSet used to retrieve root query
     * @param rowsPerSplit number of rows per one query split
     * @return list of split queries
     */
    List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit );

    /**
     * Retrieve list of {@link EntityExportData} for specified sql query
     *
     * @param sql query
     * @return list of {@link EntityExportData}
     */
    List<EntityExportData> retrieveEntityMetaDataList( String sql );

    /**
     * Import data to agent data source
     *
     * @param importSet {@link ImportSet}
     */
    void importData( ImportSet importSet );
}
