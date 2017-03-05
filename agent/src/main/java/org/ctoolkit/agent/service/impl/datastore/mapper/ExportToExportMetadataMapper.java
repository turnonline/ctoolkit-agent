package org.ctoolkit.agent.service.impl.datastore.mapper;

import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.resource.ExportBatch;
import org.ctoolkit.agent.resource.ExportJob;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ExportBatch} to {@link ExportMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ExportToExportMetadataMapper
        extends BaseSetToBaseMetadataMapper<ExportBatch, ExportMetadata, ExportBatch.ExportItem, ExportMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

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
    protected void extraMapBToA( ExportMetadata metadata, ExportBatch set )
    {
        set.setJobInfo( ( ExportJob ) changeSetService.getJobInfo( metadata ) );
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
