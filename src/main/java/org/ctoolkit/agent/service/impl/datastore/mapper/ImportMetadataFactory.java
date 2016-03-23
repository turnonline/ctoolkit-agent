package org.ctoolkit.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.model.Import;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportMetadataFactory
        implements ObjectFactory<ImportMetadata>
{
    private final DataAccess dataAccess;

    @Inject
    public ImportMetadataFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ImportMetadata create( Object o, MappingContext mappingContext )
    {
        Import asImport = ( Import ) o;
        if ( asImport.getKey() != null )
        {
            return dataAccess.find( ImportMetadata.class, asImport.getKey() );
        }

        return new ImportMetadata();
    }
}
