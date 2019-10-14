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

import org.apache.commons.lang.NotImplementedException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Kafka implementation of {@link Connector}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
@Named("kafka")
public class KafkaConnector implements Connector
{
    @Override
    public void push( String connectionString, Object payload )
    {
        throw new NotImplementedException( "Pushing to kafka is not supported yet." );
    }

    @Override
    public Object pull( String connectionString, Map<String, String> queryParams )
    {
        throw new NotImplementedException( "Pulling from kafka is not supported yet." );
    }
}
