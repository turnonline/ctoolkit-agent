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
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.inject.Injector;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.service.impl.datastore.KeyProvider;
import org.ctoolkit.agent.util.XmlUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;

/**
 * Base metadata item
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 * @see {@link ImportMetadataItem}
 */
public abstract class BaseMetadataItem<PARENT extends BaseMetadata>
        extends BaseEntity
        implements Convertible, Comparable<BaseMetadataItem<PARENT>>
{
    @Inject
    private static Injector injector;

    @Inject
    @BucketName
    private static String bucketName;

    private Key key;

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
        this.key = injector.getInstance( KeyProvider.class ).key( this, getMetadata() );
    }

    public PARENT getMetadata()
    {
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

    @Override
    public void convert( Entity entity )
    {
        setId( entity.getKey().getId() );
        setName( entity.getString( "name" ) );
        setFileName( entity.getString( "fileName" ) );
        setDataType( entity.contains( "dataType" ) ? ISetItem.DataType.valueOf( entity.getString( "dataType" ) ) : null );
        setState( entity.contains( "state" ) ? JobState.valueOf( entity.getString( "state" ) ) : null );
        setError( entity.contains( "error" ) ? new String( entity.getBlob( "error" ).toByteArray(), Charsets.UTF_8 ) : null );

        key = entity.getKey();
        dataLength = entity.contains( "dataLength" ) ? entity.getLong( "dataLength" ) : 0;
    }

    public void save()
    {
        // save fields
        saveFieldsOnly();

        // save data
        if ( getData() != null )
        {
            // put data to storage
            BlobInfo blobInfo = BlobInfo
                    .newBuilder( bucketName, getFileName() )
                    .setContentType( getDataType().mimeType() )
                    .build();
            storage().create( blobInfo, getData() );
        }
    }

    public void saveFieldsOnly()
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

        if ( getError() != null )
        {
            builder.set( "error", Blob.copyFrom( getError().getBytes( Charsets.UTF_8 ) ) );
        }

        builder.set( "dataLength", dataLength );

        if ( getFileName() == null )
        {
            setFileName( newFileName() );
        }

        builder.set( "fileName", getFileName() );

        // save additional fields
        saveFieldsOnlyAdditional( builder );

        // put item to datastore
        datastore().put( builder.build() );
    }

    protected void saveFieldsOnlyAdditional( FullEntity.Builder<IncompleteKey> builder )
    {
        // noop
    }

    public void delete()
    {
        // delete item from datastore
        datastore().delete( key() );

        // delete data from storage
        storage().delete( BlobId.of( bucketName, fileName ) );
    }

    protected Datastore datastore()
    {
        return injector.getInstance( Datastore.class );
    }

    protected Storage storage()
    {
        return injector.getInstance( Storage.class );
    }

    public Key key()
    {
        return key;
    }

    public <T> T unmarshallData(Class<T> clazz)
    {
        switch ( getDataType() )
        {
            case JSON:
            {
                return new Gson().fromJson( new String( data, Charsets.UTF_8 ), clazz );
            }
            case XML:
            {
                return XmlUtils.unmarshall( new ByteArrayInputStream( data ), clazz );
            }
        }

        throw new IllegalArgumentException( "Unknown data type: '" + getDataType() + "'" );
    }

    @Override
    public int compareTo( BaseMetadataItem<PARENT> other )
    {
        return this.getName().compareTo( other.getName() );
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
