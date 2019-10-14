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

package biz.turnonline.ecosystem.service.connector;

import java.util.Map;

/**
 * Unified connector api for various connectors like Rest, Kafka, GCP Pubsub.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface Connector
{
    /**
     * Push data to target connector
     *
     * @param connectionString definition of connection string - for rest connector it is url, for kafka it is kafka broker url + topic name
     * @param payload payload send to target connector
     */
    void push(String connectionString, Object payload);

    /**
     * Retrieve data from target connector. Retrieved data are expected to be application/json content type.
     *
     * @param connectionString definition of connection string - for rest connector it is url, for kafka it is kafka broker url + topic name
     * @param queryParams query parameters passed to connection string - for rest it is query parameters
     * @return response payload
     */
    Object pull( String connectionString, Map<String, String> queryParams );
}
