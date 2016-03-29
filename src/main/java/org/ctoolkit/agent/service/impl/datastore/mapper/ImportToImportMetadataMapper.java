package org.ctoolkit.agent.service.impl.datastore.mapper;

import org.ctoolkit.agent.model.ImportBatch;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;

/**
 * Mapper for {@link ImportBatch} to {@link ImportMetadata} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportToImportMetadataMapper
        extends BaseSetToBaseMetadataMapper<ImportBatch, ImportMetadata, ImportBatch.ImportItem, ImportMetadataItem>
{
    @Override
    protected ImportBatch.ImportItem newItem()
    {
        return new ImportBatch.ImportItem();
    }

    @Override
    protected void addItem( ImportBatch anImport, ImportBatch.ImportItem anItem )
    {
        anImport.getItems().add( anItem );
    }
}
