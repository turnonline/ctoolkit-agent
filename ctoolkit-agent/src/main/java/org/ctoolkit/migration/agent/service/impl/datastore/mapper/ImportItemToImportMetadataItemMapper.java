package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import org.ctoolkit.migration.agent.model.ImportBatch;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;

/**
 * Mapper for {@link ImportBatch.ImportItem} to {@link ImportMetadataItem} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportItemToImportMetadataItemMapper
        extends BaseSetItemToBaseMetadataItemMapper<ImportBatch.ImportItem, ImportMetadataItem>
{
}
