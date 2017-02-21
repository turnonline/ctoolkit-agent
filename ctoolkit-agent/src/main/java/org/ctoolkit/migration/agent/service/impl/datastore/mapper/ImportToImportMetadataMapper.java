package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import org.ctoolkit.migration.agent.model.ImportBatch;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Mapper for {@link ImportBatch} to {@link ImportMetadata} model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportToImportMetadataMapper
        extends BaseSetToBaseMetadataMapper<ImportBatch, ImportMetadata, ImportBatch.ImportItem, ImportMetadataItem>
{
    @Inject
    private ChangeSetService changeSetService;

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

    @Override
    protected void extraMapBToA( ImportMetadata metadata, ImportBatch set )
    {
        set.setJobInfo( ( ImportJobInfo ) changeSetService.getJobInfo( metadata ) );
    }
}
