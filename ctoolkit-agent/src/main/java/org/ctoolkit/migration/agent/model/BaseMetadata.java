package org.ctoolkit.migration.agent.model;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Base metadata entity.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class BaseMetadata<ITEM extends BaseMetadataItem>
        extends BaseEntity
{
    private String mapReduceJobId;

    @Index
    private String name;

    private List<Ref<ITEM>> itemsRef = new ArrayList<>();

    @Ignore
    private List<ITEM> items = new ArrayList<>();

    @Ignore
    private boolean itemsLoaded;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getMapReduceJobId()
    {
        return mapReduceJobId;
    }

    public void setMapReduceJobId( String mapReduceJobId )
    {
        this.mapReduceJobId = mapReduceJobId;
    }

    public List<ITEM> getItems()
    {
        if ( !itemsLoaded )
        {
            for ( Ref<ITEM> ref : itemsRef )
            {
                items.add( ref.get() );
            }

            itemsLoaded = true;
        }

        return items;
    }

    public int getItemsCount()
    {
        return itemsRef.size();
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

    public void reset()
    {
        for ( ITEM item : getItems() )
        {
            item.setState( JobState.RUNNING );
        }
    }

    public int getProcessedItems()
    {
        int processed = 0;

        for ( ITEM item : getItems() )
        {
            if ( item.getState() == JobState.COMPLETED_SUCCESSFULLY )
            {
                processed++;
            }
        }

        return processed;
    }

    public int getProcessedErrorItems()
    {
        int processed = 0;

        for ( ITEM item : getItems() )
        {
            if ( item.getState() == JobState.STOPPED_BY_ERROR )
            {
                processed++;
            }
        }

        return processed;
    }

    public void save()
    {
        List<ITEM> temp = Lists.newArrayList( items );
        items.clear();

        // first save parent entity to ensure that child items will have valid persistent entity with id
        ofy().save().entity( this ).now();

        // put back childs
        for ( ITEM next : temp )
        {
            ofy().save().entity( next ).now();
            items.add( next );
        }

        // now save parent one more time with item associations
        ofy().save().entity( this ).now();
    }

    @OnSave
    void updateObjectifyRefs()
    {
        // first remove the items not presented in actual list. Ref list represents datastore state.
        Iterator<Ref<ITEM>> iterator = itemsRef.iterator();

        // loaded (true) item list means that some of the item could be removed
        while ( itemsLoaded && iterator.hasNext() )
        {
            Ref<ITEM> originItem = iterator.next();

            if ( !items.contains( originItem.get() ) )
            {
                iterator.remove();
                ofy().delete().key( originItem.getKey() ).now();
            }
        }

        // add new items to be associated with this import
        for ( ITEM item : items )
        {
            Ref<ITEM> ref = Ref.create( item );
            if ( !itemsRef.contains( ref ) )
            {
                itemsRef.add( ref );
            }
        }

        items.clear();
        itemsLoaded = false;
    }

    @Override
    public String toString()
    {
        return "Metadata{" +
                ", mapReduceJobId='" + mapReduceJobId + '\'' +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
