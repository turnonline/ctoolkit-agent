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

package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyDateTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Transformer transforms date into predefined format
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class DateTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyDateTransformer>
{
    private static Logger log = LoggerFactory.getLogger( DateTransformerProcessor.class );

    @Override
    public Object transform( Object value, MigrationSetPropertyDateTransformer transformer, Map<Object, Object> ctx )
    {
        Date date = null;

        if ( value instanceof Date )
        {
            date = ( Date ) value;

            if (!transformer.getEpoch())
            {
                SimpleDateFormat sdf = new SimpleDateFormat( transformer.getFormat() );
                sdf.setTimeZone( TimeZone.getTimeZone( transformer.getTimeZone() ) );
                value = sdf.format( value );
            }
        }
        else if ( value instanceof String )
        {
            String format = transformer.getFormat();
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat( format );
                sdf.setTimeZone( TimeZone.getTimeZone( transformer.getTimeZone() ) );
                value = sdf.parse( ( String ) value );
                date = ( Date ) value;
            }
            catch ( ParseException e )
            {
                log.info( "Unable to parse value '" + value + "' from format '" + format + "' as date", e );
            }
        }

        if ( date != null && transformer.getEpoch() )
        {
            return date.getTime();
        }

        return value;
    }
}
