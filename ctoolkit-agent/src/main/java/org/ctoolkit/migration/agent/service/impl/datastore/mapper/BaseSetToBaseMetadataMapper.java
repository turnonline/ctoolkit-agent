package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.annotation.Entity;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.migration.agent.model.BaseMetadata;
import org.ctoolkit.migration.agent.model.BaseMetadataItem;
import org.ctoolkit.migration.agent.model.ISet;
import org.ctoolkit.migration.agent.model.ISetItem;

/**
 * Mapper for frontend to backend metadata model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class BaseSetToBaseMetadataMapper<F extends ISet<?>, B extends BaseMetadata<BI>, FI extends ISetItem, BI extends BaseMetadataItem<B>>
        extends CustomMapper<F, B>
{
    @Override
    @SuppressWarnings( "unchecked" )
    public void mapAtoB( F set, B metadata, MappingContext context )
    {
        metadata.setName( set.getName() );

        extraMapAToB( set, metadata );

        for ( ISetItem anItem : set.getItems() )
        {
            BI item = metadata.getItemByIdOrCreateNewOne( getMetadataItemId( anItem ) );
            item.setData( anItem.getData() );
            item.setDataType( anItem.getDataType() );
            item.setName( anItem.getName() );

            extraMapAItemToBItem( ( FI ) anItem, item );
        }
    }

    @Override
    public void mapBtoA( B metadata, F set, MappingContext context )
    {
        set.setKey( createMetadataKey( metadata ) );
        set.setName( metadata.getName() );
        set.setMapReduceJobId( metadata.getMapReduceJobId() );
        set.setCreateDate( metadata.getCreateDate() );
        set.setUpdateDate( metadata.getUpdateDate() );
        set.setToken( metadata.getToken() );

        extraMapBToA( metadata, set );

        for ( BI item : metadata.getItems() )
        {
            FI anItem = newItem();
            anItem.setKey( createMetadataItemKey( metadata, item ) );
            anItem.setName( item.getName() );
            anItem.setCreateDate( item.getCreateDate() );
            anItem.setUpdateDate( item.getUpdateDate() );
            anItem.setData( item.getData() );
            anItem.setDataType( item.getDataType() );

            extraMapBItemToAItem( item, anItem );

            addItem( set, anItem );
        }
    }

    protected void extraMapAToB( F set, B metadata )
    {

    }

    protected void extraMapBToA( B metadata, F set )
    {

    }

    protected void extraMapAItemToBItem( FI anItem, BI item )
    {

    }

    protected void extraMapBItemToAItem( BI item, FI anItem )
    {

    }

    protected abstract FI newItem();

    protected abstract void addItem( F anImport, FI anItem );

    private String createMetadataKey( B metadata )
    {
        String metadataEntityName = metadata.getClass().getAnnotation( Entity.class ).name();
        return KeyFactory.keyToString( KeyFactory.createKey( metadataEntityName, metadata.getId() ) );
    }

    private String createMetadataItemKey( B metadata, BI item )
    {
        String metadataEntityName = metadata.getClass().getAnnotation( Entity.class ).name();
        String itemEntityName = item.getClass().getAnnotation( Entity.class ).name();

        Key parentKey = KeyFactory.createKey( metadataEntityName, metadata.getId() );
        Key key = KeyFactory.createKey( parentKey, itemEntityName, item.getId() );

        return KeyFactory.keyToString( key );
    }

    private Long getMetadataItemId( ISetItem anItem )
    {
        if ( anItem.getKey() == null )
        {
            return null;
        }

        return KeyFactory.stringToKey( anItem.getKey() ).getId();
    }
}
