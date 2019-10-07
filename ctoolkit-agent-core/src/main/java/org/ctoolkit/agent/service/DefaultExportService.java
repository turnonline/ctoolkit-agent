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

import io.micronaut.context.annotation.Requires;
import org.apache.commons.lang.NotImplementedException;
import org.ctoolkit.agent.model.MigrationContext;
import org.ctoolkit.agent.model.api.MigrationSet;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link ExportService}. If concrete agent supports data export,
 * it should implement its own ExportService and thus disabling default implementation.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
@Requires(missingBeans = ExportService.class)
public class DefaultExportService implements ExportService
{
    @Override
    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        throw new NotImplementedException( "ExportService is not implemented for this agent" );
    }

    @Override
    public List<MigrationContext> executeQuery( String sql, Map<String, Object> namedParameters )
    {
        throw new NotImplementedException( "ExportService is not implemented for this agent" );
    }
}
