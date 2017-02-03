package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import org.ctoolkit.migration.agent.model.ChangeBatch;
import org.ctoolkit.migration.agent.model.ChangeJobInfo;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ChangeBatch} to {@link ChangeMetadata} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeToChangeMetadataMapper
        extends BaseSetToBaseMetadataMapper<ChangeBatch, ChangeMetadata, ChangeBatch.ChangeItem, ChangeMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

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

    @Override
    protected void extraMapBToA( ChangeMetadata metadata, ChangeBatch set )
    {
        set.setJobInfo( ( ChangeJobInfo ) changeSetService.getJobInfo( metadata ) );
    }
}
