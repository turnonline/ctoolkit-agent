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

import biz.turnonline.ecosystem.model.LatLng;
import com.google.cloud.datastore.LatLngValue;

import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class LatLngValueResolver
        implements ValueResolver<LatLng, LatLngValue>
{
    @Override
    public Map<String, LatLng> fromValue( String name, LatLngValue value )
    {
        Map<String, LatLng> map = new LinkedHashMap<>();
        map.put( name, new LatLng( value.get().getLatitude(), value.get().getLongitude() ) );

        return map;
    }

    @Override
    public LatLngValue toValue( LatLng object )
    {
        return LatLngValue.of( com.google.cloud.datastore.LatLng.of( object.getLatitude(), object.getLongitude() ) ) ;
    }
}
