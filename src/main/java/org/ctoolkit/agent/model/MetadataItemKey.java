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

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MetadataItemKey<M extends BaseMetadata<MI>, MI extends BaseMetadataItem<M>>
{
    private Long id;

    private Long metadataId;

    private Class<MI> metadataItemClass;

    private Class<M> metadataClass;

    public MetadataItemKey( Long id, Long metadataId, Class<MI> metadataItemClass, Class<M> metadataClass )
    {
        this.id = id;
        this.metadataId = metadataId;
        this.metadataItemClass = metadataItemClass;
        this.metadataClass = metadataClass;
    }

    public Long getId()
    {
        return id;
    }

    public Long getMetadataId()
    {
        return metadataId;
    }

    public Class<MI> getMetadataItemClass()
    {
        return metadataItemClass;
    }

    public Class<M> getMetadataClass()
    {
        return metadataClass;
    }

    @Override
    public String toString()
    {
        return "MetadataItemKey{" +
                "id=" + id +
                ", metadataId=" + metadataId +
                ", metadataItemClass=" + metadataItemClass +
                ", metadataClass=" + metadataClass +
                '}';
    }
}
