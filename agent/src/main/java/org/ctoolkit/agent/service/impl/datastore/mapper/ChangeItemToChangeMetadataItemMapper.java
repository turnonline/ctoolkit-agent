package org.ctoolkit.agent.service.impl.datastore.mapper;

import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.resource.ChangeBatch;

/**
 * Mapper for {@link ChangeBatch.ChangeItem} to {@link ChangeMetadataItem} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeItemToChangeMetadataItemMapper
        extends BaseSetItemToBaseMetadataItemMapper<ChangeBatch.ChangeItem, ChangeMetadataItem>
{
}
