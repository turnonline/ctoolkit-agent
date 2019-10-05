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

package org.ctoolkit.agent.service.transformer;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.ctoolkit.agent.model.api.MigrationSetPropertyBlobTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;

/**
 * Transformer transforms blob values (byte array, clob, blob) into string
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class BlobTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyBlobTransformer>
{
    private static Logger log = LoggerFactory.getLogger( BlobTransformerProcessor.class );

    @Override
    public Object transform( Object value, MigrationSetPropertyBlobTransformer transformer, Map<Object, Object> ctx )
    {
        if ( value instanceof byte[] )
        {
            value = new String( ( byte[] ) value, Charsets.UTF_8 );
        }
        else if ( value instanceof Blob )
        {
            try
            {
                InputStream stream = ( ( Blob ) value ).getBinaryStream();
                byte[] data = new byte[stream.available()];
                ByteStreams.readFully( stream, data );

                value = new String( data, Charsets.UTF_8 );
            }
            catch ( SQLException | IOException e )
            {
                log.info( "Unable to read Blob data", e );
            }
        }
        else if ( value instanceof Clob )
        {
            try
            {
                Reader reader = ( ( Clob ) value ).getCharacterStream();
                value = CharStreams.toString( reader );
            }
            catch ( SQLException | IOException e )
            {
                log.info( "Unable to read Clob data", e );
            }
        }

        return value;
    }
}
