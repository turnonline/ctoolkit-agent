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

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base metadata item
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 * @see {@link ImportMetadataItem}
 */
public abstract class BaseMetadataItem<PARENT extends BaseMetadata>
        extends BaseEntity
{
    @Parent
    private Ref<PARENT> metadataRef;

    @Ignore
    private PARENT metadata;

    private String name;

    @Ignore
    private byte[] data;

    private String fileName;

    private long dataLength;

    private ISetItem.DataType dataType;

    private JobState state;

    private String error;

    public BaseMetadataItem()
    {
    }

    public BaseMetadataItem( PARENT metadata )
    {
        this.metadata = metadata;
    }

    public static String newFileName( String key )
    {
        return "MetadataItem-" + key;
    }

    public PARENT getMetadata()
    {
        if ( metadata == null )
        {
            metadata = metadataRef.get();
        }
        return metadata;
    }

    public Ref<PARENT> getMetadataRef()
    {
        return metadataRef;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData( byte[] data )
    {
        this.data = data;
        dataLength = data.length;
    }

    public long getDataLength()
    {
        return dataLength;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public String newFileName()
    {
        return newFileName( getKey() );
    }

    public ISetItem.DataType getDataType()
    {
        return dataType;
    }

    public void setDataType( ISetItem.DataType dataType )
    {
        this.dataType = dataType;
    }

    public JobState getState()
    {
        return state;
    }

    public void setState( JobState state )
    {
        this.state = state;
    }

    public String getError()
    {
        return error;
    }

    public void setError( String error )
    {
        this.error = error;
    }

    @OnSave
    private void updateObjectifyRefs()
    {
        if ( metadataRef == null )
        {
            checkNotNull( metadata, "Metadata is mandatory to create a new persisted MetadataItem!" );
            metadataRef = Ref.create( metadata );
        }

        if ( data != null )
        {
            dataLength = data.length;
        }

        this.fileName = newFileName();
    }

    @Override
    public String toString()
    {
        return "MetadataItem{" +
                "data.length=" + ( data != null ? data.length : null ) +
                ", dataType=" + dataType +
                ", name=" + name +
                ", fileName=" + fileName +
                ", state=" + state +
                "} " + super.toString();
    }
}
