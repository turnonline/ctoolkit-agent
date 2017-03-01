package org.ctoolkit.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.model.ExportBatch;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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
