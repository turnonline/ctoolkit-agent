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

package biz.turnonline.ecosystem.converter;

import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.Value;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class EntityValueResolver
        implements ValueResolver<Value<?>, EntityValue>
{
    @Override
    public Map<String, Value<?>> fromValue( String prefixName, EntityValue value )
    {
        Map<String, Value<?>> map = new LinkedHashMap<>();
        value.get().getProperties().forEach( ( name, val ) -> map.put( prefixName + "." + name, val ) );

        return map;
    }

    @Override
    public EntityValue toValue( Value<?> object )
    {
        throw new NotImplementedException( "toValue() is not implemented for EntityValueResolver" );
    }
}
