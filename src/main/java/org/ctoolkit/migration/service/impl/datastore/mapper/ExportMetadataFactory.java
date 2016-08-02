package org.ctoolkit.migration.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.migration.model.ExportBatch;
import org.ctoolkit.migration.model.ExportMetadata;
import org.ctoolkit.migration.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ExportMetadataFactory
        implements ObjectFactory<ExportMetadata>
{
    private final DataAccess dataAccess;

    @Inject
    public ExportMetadataFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ExportMetadata create( Object o, MappingContext mappingContext )
    {
        ExportBatch asExport = ( ExportBatch ) o;
        if ( asExport.getKey() != null )
        {
            return dataAccess.find( ExportMetadata.class, asExport.getKey() );
        }

        return new ExportMetadata();
    }
}
