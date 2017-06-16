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

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.inject.Injector;
import org.ctoolkit.agent.service.impl.datastore.KeyProvider;
import org.ctoolkit.agent.service.impl.datastore.ShardedCounter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Base metadata entity.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class BaseMetadata<ITEM extends BaseMetadataItem>
        extends BaseEntity
        implements Convertible
{
    @Inject
    private static Injector injector;

    private Key key;

    private String jobId;

    private String name;

    private List<ITEM> items = new ArrayList<>();

    private boolean itemsLoaded;

    private int itemsCount = 0;

    private List<Key> itemsRef = new ArrayList<>();

    @Deprecated
    private List<String> jobContext = new ArrayList<>();

    public BaseMetadata()
    {
        this.key = injector.getInstance( KeyProvider.class ).key( this );
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId( String jobId )
    {
        this.jobId = jobId;
    }

    public List<ITEM> getItems()
    {
        if ( itemsRef.isEmpty() )
        {
            itemsLoaded = true;
        }

        if ( !itemsLoaded )
        {
            Iterator<Entity> entityIterator = datastore().get( itemsRef );
            while ( entityIterator.hasNext() )
            {
                items.add( ModelConverter.convert( itemClass(), entityIterator.next() ) );
            }

            // sort by name
            Collections.sort( items );

            itemsLoaded = true;
        }

        return items;
    }

    public List<KeyValue> getItemsKeyValue()
    {
        List<KeyValue> listKeyValue = new ArrayList<>();

        for ( ITEM item : getItems() )
        {
            listKeyValue.add( new KeyValue( item.key() ) );
        }

        return listKeyValue;
    }

    public void addItem( ITEM item )
    {
        items.add( item );
    }

    public void setItemsCount( int itemsCount )
    {
        this.itemsCount = itemsCount;
    }

    public void incrementItemsCount()
    {
        itemsCount++;
    }

    public int getItemsCount()
    {
        return itemsCount;
    }

    public ITEM getItemByIdOrCreateNewOne( Long id )
    {
        if ( id != null )
        {
            for ( ITEM item : getItems() )
            {
                if ( item.getId().equals( id ) )
                {
                    return item;
                }
            }
        }

        ITEM item = newItem();
        getItems().add( item );

        return item;
    }

    protected abstract ITEM newItem();

    protected abstract Class<ITEM> itemClass();

    public void reset()
    {
        for ( ITEM item : getItems() )
        {
            item.setState( JobState.RUNNING );
        }

        ShardedCounter.clearCounters( getClass().getSimpleName(), getId() );
    }

    public int getProcessedItems()
    {
        if ( getId() == null )
        {
            return 0;
        }
        return ( int ) ShardedCounter.okCounter( getClass().getSimpleName(), getId() ).getCount();
    }

    public int getProcessedErrorItems()
    {
        if ( getId() == null )
        {
            return 0;
        }
        return ( int ) ShardedCounter.errorCounter( getClass().getSimpleName(), getId() ).getCount();
    }

    @Deprecated
    public Map<String, String> getJobContext()
    {
        Map<String, String> ctx = new HashMap<>();
        for ( String item : jobContext )
        {
            String[] split = item.split( "\\::" );
            ctx.put( split[0], split[1] );
        }

        return ctx;
    }

    @Deprecated
    public void clearJobContext()
    {
        jobContext = new ArrayList<>();
    }

    @Deprecated
    public void putToJobContext( String key, String value )
    {
        jobContext.add( key + "::" + value );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void convert( Entity entity )
    {
        setId( entity.getKey().getId() );
        setName( entity.contains( "name" ) ? entity.getString( "name" ) : null );
        setJobId( entity.contains( "jobId" ) ? entity.getString( "jobId" ) : null );
        setItemsCount( entity.contains( "itemsCount" ) ? ( ( Long ) entity.getValue( "itemsCount" ).get() ).intValue() : 0 );

        key = entity.getKey();

        List<KeyValue> itemsRefKeyValue = entity.contains( "itemsRef" ) ? ( List<KeyValue> ) entity.getValue( "itemsRef" ).get() : new ArrayList<KeyValue>();
        itemsRef = new ArrayList<>();

        for ( KeyValue keyValue : itemsRefKeyValue )
        {
            itemsRef.add( keyValue.get() );
        }
    }

    public void save()
    {
        FullEntity.Builder<IncompleteKey> builder = Entity.newBuilder();
        builder.setKey( key() );

        if ( getName() != null )
        {
            builder.set( "name", getName() );
        }

        if ( getJobId() != null )
        {
            builder.set( "jobId", getJobId() );
        }

        // add new items
        setItemsCount( 0 );
        for ( ITEM item : getItems() )
        {
            item.save();
            incrementItemsCount();
        }

        builder.set( "itemsRef", getItemsKeyValue() );
        builder.set( "itemsCount", getItemsCount() );

        // put metadata to datastore
        datastore().put( builder.build() );
    }

    public void delete()
    {
        // remove items
        if ( !itemsRef.isEmpty() )
        {
            for ( Entity item : datastore().fetch( itemsRef ) )
            {
                ModelConverter.convert( itemClass(), item ).delete();
            }
        }

        // remove metadata
        datastore().delete( key() );
    }

    protected Datastore datastore()
    {
        return injector.getInstance( Datastore.class );
    }

    public Key key()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "BaseMetadata{" +
                "jobId='" + jobId + '\'' +
                ", name='" + name + '\'' +
                ", itemsLoaded=" + itemsLoaded +
                ", itemsCount=" + itemsCount +
                ", jobContext=" + jobContext +
                "} " + super.toString();
    }
}
