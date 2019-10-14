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

import org.apache.beam.repackaged.beam_runners_core_java.com.google.common.base.Charsets;
import org.apache.beam.repackaged.beam_runners_core_java.com.google.common.io.BaseEncoding;
import org.ctoolkit.agent.model.api.MigrationSetPropertyEncodingTransformer;

import java.util.Map;

/**
 * Transformer transforms blob values (byte array, clob, blob) into string
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class EncodingTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyEncodingTransformer>
{
    @Override
    public Object transform( Object value, MigrationSetPropertyEncodingTransformer transformer, Map<String, Object> ctx )
    {
        BaseEncoding encoding = null;

        switch ( transformer.getEncodingType() )
        {
            case "base16":
            {
                encoding = BaseEncoding.base16();
                break;
            }
            case "base32":
            {
                encoding = BaseEncoding.base32();
                break;
            }
            case "base32Hex":
            {
                encoding = BaseEncoding.base32Hex();
                break;
            }
            case "base64":
            {
                encoding = BaseEncoding.base64();
                break;
            }
            case "base64Url":
            {
                encoding = BaseEncoding.base64Url();
                break;
            }
        }

        if ( encoding != null )
        {
            switch ( transformer.getOperation() )
            {
                case "encode":
                {
                    if ( value instanceof String )
                    {
                        value = ( ( String ) value ).getBytes( Charsets.UTF_8 );
                    }
                    if ( value instanceof byte[] )
                    {
                        value = encoding.encode( ( byte[] ) value );
                    }

                    break;
                }
                case "decode":
                {
                    if ( value instanceof byte[] )
                    {
                        value = new String( ( byte[] ) value, Charsets.UTF_8 );
                    }
                    if ( value instanceof String )
                    {
                        value = new String( encoding.decode( ( String ) value ), Charsets.UTF_8 );
                    }
                    break;
                }
            }
        }

        return value;
    }
}
