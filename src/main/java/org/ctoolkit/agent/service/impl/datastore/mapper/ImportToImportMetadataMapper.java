package org.ctoolkit.agent.service.impl.datastore.mapper;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.agent.model.Import;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;

/**
 * Mapper for {@link Import} to {@link ImportMetadata} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportToImportMetadataMapper
        extends CustomMapper<Import, ImportMetadata>
{
    @Override
    public void mapAtoB( Import anImport, ImportMetadata importMetadata, MappingContext context )
    {
        for ( Import.Item anItem : anImport.getItems() )
        {
            ImportMetadataItem item = importMetadata.getItemByIdOrCreateNewOne( getImportMetadataItemId( anItem ) );
            item.setXml( anItem.getXml() );
        }
    }

    @Override
    public void mapBtoA( ImportMetadata importMetadata, Import anImport, MappingContext context )
    {
        anImport.setKey( createImportMetadataKey( importMetadata ) );
        anImport.setMapReduceJobId( importMetadata.getMapReduceJobId() );
        anImport.setCreateDate( importMetadata.getCreateDate() );
        anImport.setUpdateDate( importMetadata.getUpdateDate() );

        for ( ImportMetadataItem item : importMetadata.getItems() )
        {
            Import.Item anItem = new Import.Item();
            anItem.setKey( createImportMetadataItemKey( importMetadata, item ) );
            anItem.setXml( item.getXml() );
            anItem.setCreateDate( item.getCreateDate() );
            anItem.setUpdateDate( item.getUpdateDate() );
            anImport.getItems().add( anItem );
        }
    }

    private String createImportMetadataKey( ImportMetadata importMetadata )
    {
        return KeyFactory.keyToString( KeyFactory.createKey( ImportMetadata.class.getSimpleName(), importMetadata.getId() ) );
    }

    private String createImportMetadataItemKey( ImportMetadata importMetadata, ImportMetadataItem item )
    {
        Key parentKey = KeyFactory.createKey( ImportMetadata.class.getSimpleName(), importMetadata.getId() );
        Key key = KeyFactory.createKey( parentKey, ImportMetadataItem.class.getSimpleName(), item.getId() );

        return KeyFactory.keyToString( key );
    }

    private Long getImportMetadataItemId( Import.Item anItem )
    {
        if ( anItem.getKey() == null )
        {
            return null;
        }

        return KeyFactory.stringToKey( anItem.getKey() ).getId();
    }
}
