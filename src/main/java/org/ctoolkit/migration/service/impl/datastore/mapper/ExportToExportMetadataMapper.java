package org.ctoolkit.migration.service.impl.datastore.mapper;

import org.ctoolkit.migration.model.ExportBatch;
import org.ctoolkit.migration.model.ExportMetadata;
import org.ctoolkit.migration.model.ExportMetadataItem;

/**
 * Mapper for {@link ExportBatch} to {@link ExportMetadata} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ExportToExportMetadataMapper
        extends BaseSetToBaseMetadataMapper<ExportBatch, ExportMetadata, ExportBatch.ExportItem, ExportMetadataItem>
{
    @Override
    protected ExportBatch.ExportItem newItem()
    {
        return new ExportBatch.ExportItem();
    }

    @Override
    protected void addItem( ExportBatch anImport, ExportBatch.ExportItem anItem )
    {
        anImport.getItems().add( anItem );
    }

    @Override
    protected void extraMapAItemToBItem( ExportBatch.ExportItem anItem, ExportMetadataItem item )
    {
        item.setEntityToExport( anItem.getEntityToExport() );
    }

    @Override
    protected void extraMapBItemToAItem( ExportMetadataItem item, ExportBatch.ExportItem anItem )
    {
        anItem.setEntityToExport( item.getEntityToExport() );
    }
}
