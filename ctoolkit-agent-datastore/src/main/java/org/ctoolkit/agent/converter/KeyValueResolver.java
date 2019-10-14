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

package org.ctoolkit.agent.converter;

import com.google.cloud.datastore.KeyValue;
import org.ctoolkit.agent.model.RawKey;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class KeyValueResolver
        implements ValueResolver<RawKey, KeyValue>
{
    private final KeyConverter keyConverter;

    @Inject
    public KeyValueResolver( KeyConverter keyConverter )
    {
        this.keyConverter = keyConverter;
    }

    @Override
    public Map<String, RawKey> fromValue( String name, KeyValue value )
    {
        return Collections.singletonMap( name, new RawKey( keyConverter.convertFromKey( value.get() ) ) );
    }

    @Override
    public KeyValue toValue( RawKey object )
    {
        return KeyValue.of( keyConverter.convertFromRawKey( object.toString() ) );
    }
}
