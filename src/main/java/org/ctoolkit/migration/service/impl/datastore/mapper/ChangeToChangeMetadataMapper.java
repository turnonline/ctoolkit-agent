package org.ctoolkit.migration.service.impl.datastore.mapper;

import org.ctoolkit.migration.model.ChangeBatch;
import org.ctoolkit.migration.model.ChangeMetadata;
import org.ctoolkit.migration.model.ChangeMetadataItem;

/**
 * Mapper for {@link ChangeBatch} to {@link ChangeMetadata} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeToChangeMetadataMapper
        extends BaseSetToBaseMetadataMapper<ChangeBatch, ChangeMetadata, ChangeBatch.ChangeItem, ChangeMetadataItem>
{
    @Override
    protected ChangeBatch.ChangeItem newItem()
    {
        return new ChangeBatch.ChangeItem();
    }

    @Override
    protected void addItem( ChangeBatch anImport, ChangeBatch.ChangeItem anItem )
    {
        anImport.getItems().add( anItem );
    }
}
