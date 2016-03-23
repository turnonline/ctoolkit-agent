package org.ctoolkit.agent.model;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.OnSave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Import metadata entity.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity
public class ImportMetadata
        extends BaseEntity
{
    private String mapReduceJobId;

    private List<Ref<ImportMetadataItem>> itemsRef = new ArrayList<>();

    @Ignore
    private List<ImportMetadataItem> items = new ArrayList<>();

    @Ignore
    private boolean itemsLoaded;

    public String getMapReduceJobId()
    {
        return mapReduceJobId;
    }

    public void setMapReduceJobId( String mapReduceJobId )
    {
        this.mapReduceJobId = mapReduceJobId;
    }

    public List<ImportMetadataItem> getItems()
    {
        if ( !itemsLoaded )
        {
            for ( Ref<ImportMetadataItem> ref : itemsRef )
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

    public ImportMetadataItem getItemByIdOrCreateNewOne( Long id )
    {
        if ( id != null )
        {
            for ( ImportMetadataItem item : getItems() )
            {
                if ( item.getId().equals( id ) )
                {
                    return item;
                }
            }
        }

        ImportMetadataItem item = new ImportMetadataItem( this );
        getItems().add( item );
        return item;
    }

    public void save()
    {
        List<ImportMetadataItem> temp = Lists.newArrayList( items );
        items.clear();

        // first save parent entity to ensure that child items will have valid persistent entity with id
        ofy().save().entity( this ).now();

        // put back childs
        for ( ImportMetadataItem next : temp )
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
        Iterator<Ref<ImportMetadataItem>> iterator = itemsRef.iterator();

        // loaded (true) item list means that some of the item could be removed
        while ( itemsLoaded && iterator.hasNext() )
        {
            Ref<ImportMetadataItem> originItem = iterator.next();

            if ( !items.contains( originItem.get() ) )
            {
                iterator.remove();
            }
        }

        // add new items to be associated with this import
        for ( ImportMetadataItem item : items )
        {
            Ref<ImportMetadataItem> ref = Ref.create( item );
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
        return "ImportMetadata{" +
                "items=" + items +
                ", mapReduceJobId='" + mapReduceJobId + '\'' +
                "} " + super.toString();
    }
}
