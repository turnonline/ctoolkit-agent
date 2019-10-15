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

package biz.turnonline.ecosystem.service.beam.options;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

import java.util.Map;

/**
 * Raw pipeline options contains all input parameters provided by PipelineOptions key-value pair from MigrationBatch which
 * starts with xoptions, also all system parameters provided by JAVA_OPTS which starts with xoptions* will be added (without xoptions prefix)
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ExtraOptionsPipelineOptions
        extends PipelineOptions
{
    @Description( "Raw pipeline options. Contains all options provided by MigrationBatch.pipelineOptions (including omitted)" +
            "and all system properties which starts with 'options.' prefix." )
    Map<String, String> getExtraOptions();
    void setExtraOptions( Map<String, String> url );
}
