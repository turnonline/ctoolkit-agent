package org.ctoolkit.agent.service.impl.datastore.mapper;

import org.ctoolkit.agent.model.ChangeBatch;
import org.ctoolkit.agent.model.ChangeJobInfo;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ChangeBatch} to {@link ChangeMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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
