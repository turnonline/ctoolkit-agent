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

package org.ctoolkit.agent;

import org.ctoolkit.agent.converter.BlobValueResolver;
import org.ctoolkit.agent.converter.BooleanValueResolver;
import org.ctoolkit.agent.converter.DoubleValueResolver;
import org.ctoolkit.agent.converter.EntityValueResolver;
import org.ctoolkit.agent.converter.KeyConverter;
import org.ctoolkit.agent.converter.KeyValueResolver;
import org.ctoolkit.agent.converter.LatLngValueResolver;
import org.ctoolkit.agent.converter.ListValueResolver;
import org.ctoolkit.agent.converter.LongValueResolver;
import org.ctoolkit.agent.converter.StringValueResolver;
import org.ctoolkit.agent.converter.TimestampValueResolver;
import org.ctoolkit.agent.converter.ValueConverter;
import org.ctoolkit.agent.service.DatastoreAgentConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Mocks for unit tests
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Mocks
{
    private static KeyConverter keyConverter = new KeyConverter( "test" );

    private static StringValueResolver stringValueResolver = new StringValueResolver();
    private static BooleanValueResolver booleanValueResolver = new BooleanValueResolver();
    private static DoubleValueResolver doubleValueResolver = new DoubleValueResolver();
    private static LongValueResolver longValueResolver = new LongValueResolver();
    private static TimestampValueResolver timestampValueResolver = new TimestampValueResolver();
    private static LatLngValueResolver latLngValueResolver = new LatLngValueResolver();
    private static BlobValueResolver blobValueResolver = new BlobValueResolver();

    private static KeyValueResolver keyValueResolver = new KeyValueResolver( keyConverter );
    private static EntityValueResolver entityValueResolver = new EntityValueResolver();
    private static ListValueResolver listValueResolver = new ListValueResolver();

    private static ValueConverter valueConverter = new ValueConverter( new DatastoreAgentConfig()
            .createValueResolvers(
                    stringValueResolver,
                    booleanValueResolver,
                    doubleValueResolver,
                    longValueResolver,
                    timestampValueResolver,
                    latLngValueResolver,
                    blobValueResolver,

                    keyValueResolver,
                    entityValueResolver,
                    listValueResolver )
    );

    public static Date date( int day, int month, int year )
    {
        return Date.from( LocalDate.of( year, month, day )
                .atStartOfDay()
                .atZone( ZoneId.of( "CET" ) )
                .toInstant() );
    }

    public static Date date( int day, int month, int year, int hour, int minute, int second )
    {
        return Date.from( LocalDateTime.of( year, month, day, hour, minute, second )
                .atZone( ZoneId.of( "CET" ) )
                .toInstant() );
    }

    public static ValueConverter valueConverter()
    {
        return valueConverter;
    }

    public static KeyConverter keyConverter()
    {
        return keyConverter;
    }
}
