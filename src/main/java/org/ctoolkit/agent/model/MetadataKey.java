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
public class MetadataKey<M extends BaseMetadata>
{
    private String key;

    private Class<M> metadataClass;

    public MetadataKey( String key, Class<M> metadataClass )
    {
        this.key = key;
        this.metadataClass = metadataClass;
    }

    public String getKey()
    {
        return key;
    }

    public Class<M> getMetadataClass()
    {
        return metadataClass;
    }

    @Override
    public String toString()
    {
        return "MetadataKey{" +
                "key='" + key + '\'' +
                ", metadataClass=" + metadataClass +
                '}';
    }
}