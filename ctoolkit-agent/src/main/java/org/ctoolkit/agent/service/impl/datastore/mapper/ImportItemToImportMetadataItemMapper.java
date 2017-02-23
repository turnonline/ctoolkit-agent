package org.ctoolkit.agent.service.impl.datastore.mapper;

import org.ctoolkit.agent.model.ImportBatch;
import org.ctoolkit.agent.model.ImportMetadataItem;

/**
 * Mapper for {@link ImportBatch.ImportItem} to {@link ImportMetadataItem} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportItemToImportMetadataItemMapper
        extends BaseSetItemToBaseMetadataItemMapper<ImportBatch.ImportItem, ImportMetadataItem>
{
}
