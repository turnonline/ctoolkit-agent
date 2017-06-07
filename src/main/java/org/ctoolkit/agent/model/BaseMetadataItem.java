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

import com.google.api.client.util.Base64;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.inject.Injector;
import org.ctoolkit.agent.service.impl.datastore.KeyProvider;

import javax.inject.Inject;

/**
 * Base metadata item
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 * @see {@link ImportMetadataItem}
 */
public abstract class BaseMetadataItem<PARENT extends BaseMetadata>
        extends BaseEntity
{
    @Inject
    private static Injector injector;

    private PARENT metadata;

    private String name;

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

    @SuppressWarnings( "unchecked" )
    public static <PARENT extends BaseMetadata> BaseMetadataItem<PARENT> convert( Entity entity, Class<?> clazz )
    {
        BaseMetadataItem<PARENT> metadataItem;

        try
        {
            metadataItem = ( BaseMetadataItem<PARENT> ) clazz.newInstance();
            metadataItem.convert( entity );
        }
        catch ( InstantiationException | IllegalAccessException e )
        {
            throw new RuntimeException( "Unable to create new instance of metadataItem" );
        }

        return metadataItem;
    }

    protected void convert( Entity entity )
    {
        setId( entity.getKey().getId() );
        setName( entity.getString( "name" ) );
        setFileName( entity.getString( "fileName" ) );
        setDataType( entity.contains( "dataType" ) ? ISetItem.DataType.valueOf( entity.getString( "dataType" ) ) : null );
        setState( entity.contains( "state" ) ? JobState.valueOf( entity.getString( "state" ) ) : null );
        setError( entity.contains( "error" ) ? entity.getString( "error" ) : null);
    }

    public PARENT getMetadata()
    {
        if ( metadata == null )
        {
            // TODO: implement
        }
        return metadata;
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

    public static String newFileName( String key )
    {
        return "MetadataItem-" + key;
    }

    public String newFileName()
    {
        return newFileName( Base64.encodeBase64String( key().toUrlSafe().getBytes() ) );
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

    public void save()
    {
        FullEntity.Builder<IncompleteKey> builder = Entity.newBuilder();
        builder.setKey( key() );
        builder.set( "name", getName() );

        if ( getDataType() != null )
        {
            builder.set( "dataType", getDataType().name() );
        }

        if ( getState() != null )
        {
            builder.set( "state", getState().name() );
        }

        if (getError() != null)
        {
            builder.set( "error", getError() );
        }

        if ( getData() != null )
        {
            builder.set( "dataLength", data.length );
        }

        if ( getId() == null )
        {
            builder.set( "fileName", newFileName() );
        }

        datastore().put( builder.build() );

        // TODO: store data to storage
    }

    protected Datastore datastore()
    {
        return injector.getInstance( Datastore.class );
    }

    public Key key()
    {
        return injector.getInstance( KeyProvider.class ).key( this, getMetadata() );
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
