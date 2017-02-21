package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.migration.agent.model.ImportBatch;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportMetadataItemFactory
        implements ObjectFactory<ImportMetadataItem>
{
    private final DataAccess dataAccess;

    @Inject
    public ImportMetadataItemFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ImportMetadataItem create( Object o, MappingContext mappingContext )
    {
        ImportBatch.ImportItem asImportItem = ( ImportBatch.ImportItem ) o;
        if ( asImportItem.getKey() != null )
        {
            return dataAccess.find( ImportMetadataItem.class, asImportItem.getKey() );
        }

        String metadataId = ( String ) mappingContext.getProperty( "metadataId" );
        ImportMetadata importMetadata = dataAccess.find( ImportMetadata.class, metadataId );
        return new ImportMetadataItem( importMetadata );
    }
}
