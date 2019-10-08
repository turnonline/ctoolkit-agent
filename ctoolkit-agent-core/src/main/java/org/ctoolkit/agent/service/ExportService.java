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

import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.util.List;
import java.util.Map;

/**
 * Export service is used by concrete agent which supports data export
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ExportService
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
     * Retrieve list of {@link Export} for specified sql query
     *
     * @param sql             query
     * @param namedParameters named parameters used to replace parameters in query
     * @return list of {@link Export}
     */
    List<Export> executeQuery( String sql, Map<String, Object> namedParameters );
}
