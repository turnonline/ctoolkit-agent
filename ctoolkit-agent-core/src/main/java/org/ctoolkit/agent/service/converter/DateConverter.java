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

package org.ctoolkit.agent.service.converter;

import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Date converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DateConverter
        implements Converter
{
    private static final Logger log = LoggerFactory.getLogger( DateConverter.class );

    public static DateConverter INSTANCE = new DateConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        Date target = null;
        try
        {
            if ( source instanceof Date )
            {
                target = ( Date ) source;
            }
            else
            {
                target = new Date( Long.valueOf( source.toString() ) );
            }
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create date from value: '" + source + "'", e );
        }

        return target != null ? Long.valueOf( target.getTime() ).toString() : null;
    }

    @Override
    public Date convert( ImportSetProperty property )
    {
        Date target = null;
        try
        {
            target = new Date( Long.valueOf( property.getValue() ) );
        }
        catch ( NumberFormatException e )
        {
            log.info( "Unable to create date from value: '" + property.getValue() + "'", e );
        }

        return target;
    }
}
