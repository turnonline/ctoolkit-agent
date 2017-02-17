package org.ctoolkit.migration.agent.model;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private int itemsCount;

    private List<Long> processedOk = new ArrayList<>();

    private List<Long> processedError = new ArrayList<>();

    private List<String> jobContext = new ArrayList<>();

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
            items.addAll( new ArrayList<>( ofy().load().refs( itemsRef ).values() ) );
            itemsLoaded = true;
        }

        return items;
    }

    public void addItem( ITEM item )
    {
        items.add( item );
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

    public void reset()
    {
        for ( ITEM item : getItems() )
        {
            item.setState( JobState.RUNNING );
        }

        processedOk = new ArrayList<>();
        processedError = new ArrayList<>();
    }

    public int getProcessedItems()
    {
        return processedOk.size();
    }

    public int getProcessedErrorItems()
    {
        return processedError.size();
    }

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

    public void clearJobContext()
    {
        jobContext = new ArrayList<>();
    }

    public void putToJobContext( String key, String value )
    {
        jobContext.add( key + "::" + value );
    }

    public void save()
    {
        List<ITEM> temp = Lists.newArrayList( items );
        items.clear();

        // first save parent entity to ensure that child items will have valid persistent entity with id
        ofy().save().entity( this ).now();

        // put back childs
        itemsCount = temp.size();
        ofy().save().entities( temp ).now();
        items.addAll( temp );

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

        itemsCount = itemsRef.size();
        items.clear();
        itemsLoaded = false;
    }

    public List<ITEM> getOrphans()
    {
        List<ITEM> orphans = new ArrayList<>();
        Iterator<Ref<ITEM>> iterator = itemsRef.iterator();

        while ( itemsLoaded && iterator.hasNext() )
        {
            Ref<ITEM> originItem = iterator.next();

            ITEM orphan = originItem.get();
            if ( !items.contains( orphan ) )
            {
                orphans.add( orphan );
            }
        }

        return orphans;
    }

    @Override
    public String toString()
    {
        return "BaseMetadata{" +
                "mapReduceJobId='" + mapReduceJobId + '\'' +
                ", name='" + name + '\'' +
                ", itemsLoaded=" + itemsLoaded +
                ", itemsCount=" + itemsCount +
                ", processedOk=" + processedOk +
                ", processedError=" + processedError +
                ", jobContext=" + jobContext +
                "} " + super.toString();
    }
}
