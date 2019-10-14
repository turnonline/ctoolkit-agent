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

import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Double converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DoubleConverter
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( DoubleConverter.class );

    public static DoubleConverter INSTANCE = new DoubleConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        Double target = null;
        try
        {
            target = new Double( source.toString() );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create double from value: '" + source + "'", e );
        }

        return String.valueOf( target );
    }

    @Override
    public Double convert( ImportSetProperty property )
    {
        Double target = null;
        try
        {
            target = property.getValue() != null ? new Double( property.getValue().toString() ) : null;
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create double from value: '" + property.getValue() + "'", e );
        }

        return target;
    }
}
