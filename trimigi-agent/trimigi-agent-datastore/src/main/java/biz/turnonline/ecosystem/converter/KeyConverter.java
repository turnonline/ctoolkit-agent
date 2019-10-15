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

import com.google.cloud.datastore.Key;
import com.google.datastore.v1.Key.PathElement;

import javax.inject.Inject;
import javax.inject.Named;
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
    public KeyConverter( @Named( "projectId" ) String projectId )
    {
        this.projectId = projectId;
    }

    public String convertFromKey( Key key )
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

    public Key convertFromRawKey( String rawKey )
    {
        List<String> ancestors = Arrays.asList( rawKey.trim().split( ">" ) );

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

    public List<PathElement> convertToPathElements( String rawKey )
    {
        List<PathElement> pathElements = new ArrayList<>();

        Key key = convertFromRawKey( rawKey );
        key.getAncestors().forEach( pathElement -> {
                    PathElement.Builder builder = PathElement.newBuilder().setKind( pathElement.getKind() );

                    if ( pathElement.getName() != null )
                    {
                        builder.setName( pathElement.getName() );
                    }
                    else
                    {
                        builder.setId( pathElement.getId() );
                    }

                    pathElements.add( builder.build() );
                }
        );

        PathElement.Builder builder = PathElement.newBuilder();
        if ( key.getId() != null )
        {
            builder.setId( key.getId() );
        }
        if ( key.getName() != null )
        {
            builder.setName( key.getName() );
        }
        builder.setKind( key.getKind() );

        pathElements.add( builder.build() );

        return pathElements;
    }

    public String convertToKeyLiteral( List<PathElement> pathElements )
    {
        StringBuilder rawKey = new StringBuilder();

        for ( PathElement pathElement : pathElements )
        {
            if ( rawKey.length() > 0 )
            {
                rawKey.append( "," );
            }

            rawKey.append( pathElement.getKind() )
                    .append( "," )
                    .append( pathElement.getName().isEmpty() ?
                            String.valueOf( pathElement.getId() ) :
                            pathElement.getName()
                    );
        }

        return rawKey.toString();
    }
}