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

package org.ctoolkit.agent.service.beam.options;

import org.apache.beam.sdk.options.ApplicationNameOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;
import org.ctoolkit.agent.model.Agent;

/**
 * Migration pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface MigrationPipelineOptions
        extends PipelineOptions, ApplicationNameOptions, JdbcPipelineOptions, ElasticsearchPipelineOptions, MongoPipelineOptions
{
    @Description( "Flag if migration should by executed in 'dry run' mode (import to target agent will not be performed and" +
            "instead will be written to console). By default value is set to 'false', which means migrated data will be send" +
            "to target agent to perform import." )
    @Default.Boolean( false )
    boolean isDryRun();
    void setDryRun( boolean dryRun );

    @Description( "Flag if logging of migrated data in 'dry mode' should be printed formatted or as single line." )
    @Default.Boolean( true )
    boolean isPrettyPrint();
    void setPrettyPrint( boolean prettyPrint );

    @Validation.Required
    @Description( "Target agent url (for instance http://localhost:8080/api/v1/" )
    String getTargetAgentUrl();
    void setTargetAgentUrl( String targetAgentUrl );

    @Validation.Required
    @Description( "Target agent type." )
    Agent getTargetAgent();
    void setTargetAgent( Agent targetAgent );

    @Validation.Required
    @Description( "Number of rows per split. How many rows should be contained in one query split." )
    @Default.Integer(100)
    int getRowsPerSplit();
    void setRowsPerSplit( int rowsPerSplit );
}
