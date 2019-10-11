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

import com.google.cloud.datastore.Key;
import org.ctoolkit.agent.annotation.ProjectId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * GCP key converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class KeyConverter
{
    private final String projectId;

    @Inject
    public KeyConverter( @ProjectId String projectId )
    {
        this.projectId = projectId;
    }

    public String convert( Key key )
    {
        StringBuilder converted = new StringBuilder();

        Key parentKey = key;

        List<Key> keyChain = new ArrayList<>();

        while ( parentKey != null )
        {
            keyChain.add( parentKey );
            parentKey = parentKey.getParent();
        }

        Collections.reverse( keyChain );

        keyChain.forEach( key1 -> {
            if ( converted.length() > 0 )
            {
                converted.append( ">" );
            }
            converted.append( key1.getKind() ).append( ":" ).append( key1.getNameOrId() );
        } );

        return converted.toString();
    }

    public Key convert( String keyRaw )
    {
        List<String> ancestors = Arrays.asList( keyRaw.trim().split( ">" ) );

        final AtomicReference<Key> parentKeyRef = new AtomicReference<>();

        ancestors.forEach( ancestor -> {
            String[] split = ancestor.split( ":" );
            String kind = split[0].trim();
            String idName = split[1].trim();

            Key parentKey = parentKeyRef.get();
            if ( parentKey == null )
            {
                try
                {
                    parentKeyRef.set( Key.newBuilder( projectId, kind, Long.parseLong( idName ) ).build() );
                }
                catch ( NumberFormatException e )
                {
                    parentKeyRef.set( Key.newBuilder( projectId, kind, idName ).build() );
                }
            }
            else
            {
                try
                {
                    parentKeyRef.set( Key.newBuilder( parentKey, kind, Long.parseLong( idName ) ).build() );
                }
                catch ( NumberFormatException e )
                {
                    parentKeyRef.set( Key.newBuilder( parentKey, kind, idName ).build() );
                }
            }
        } );

        return parentKeyRef.get();
    }
}
