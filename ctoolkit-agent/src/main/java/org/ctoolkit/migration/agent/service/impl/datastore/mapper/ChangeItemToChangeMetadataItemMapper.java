package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import org.ctoolkit.migration.agent.model.ChangeBatch;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;

/**
 * Mapper for {@link ChangeBatch.ChangeItem} to {@link ChangeMetadataItem} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeItemToChangeMetadataItemMapper
        extends BaseSetItemToBaseMetadataItemMapper<ChangeBatch.ChangeItem, ChangeMetadataItem>
{
}
