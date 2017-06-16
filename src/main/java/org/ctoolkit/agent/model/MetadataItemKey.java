/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.model;


import com.google.cloud.datastore.Key;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MetadataItemKey<M extends BaseMetadata<MI>, MI extends BaseMetadataItem<M>>
{
    @Inject
    @ProjectId
    private static String projectId;

    private Key key;

    private Class<MI> metadataItemClass;

    public MetadataItemKey( Long id, Long metadataId, Class<MI> metadataItemClass, Class<M> metadataClass )
    {
        String kind = metadataItemClass.getAnnotation( EntityMarker.class ).name();
        String parentKind = metadataClass.getAnnotation( EntityMarker.class ).name();

        Key parentKey = Key.newBuilder( projectId, parentKind, metadataId ).build();
        this.key = Key.newBuilder( parentKey, kind, id ).build();
        this.metadataItemClass = metadataItemClass;
    }

    public MetadataItemKey( Class<MI> metadataItemClass, Key key )
    {
        this.metadataItemClass = metadataItemClass;
        this.key = key;
    }

    public Key getKey()
    {
        return key;
    }

    public Class<MI> getMetadataItemClass()
    {
        return metadataItemClass;
    }

    @Override
    public String toString()
    {
        return "MetadataItemKey{" +
                "key=" + key +
                ", metadataItemClass=" + metadataItemClass +
                '}';
    }
}
