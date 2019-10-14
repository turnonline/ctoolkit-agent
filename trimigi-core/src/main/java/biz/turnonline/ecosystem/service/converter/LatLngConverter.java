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

package biz.turnonline.ecosystem.service.converter;

import biz.turnonline.ecosystem.model.LatLng;
import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Latitude/longitude converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class LatLngConverter
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( LatLngConverter.class );

    public static LatLngConverter INSTANCE = new LatLngConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        return source != null ? source.toString() : null;
    }

    @Override
    public LatLng convert( ImportSetProperty property )
    {
        LatLng target = null;
        try
        {
            if ( property.getValue() != null )
            {
                String[] split = property.getValue().toString().split( ":" );
                target = new LatLng( Double.parseDouble( split[0] ), Double.parseDouble( split[1] ) );
            }
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create LatLong from value: '" + property.getValue() + "'", e );
        }

        return target;
    }
}
