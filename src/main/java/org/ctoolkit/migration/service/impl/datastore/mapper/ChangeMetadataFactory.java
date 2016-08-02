package org.ctoolkit.migration.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.migration.model.ChangeBatch;
import org.ctoolkit.migration.model.ChangeMetadata;
import org.ctoolkit.migration.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeMetadataFactory
        implements ObjectFactory<ChangeMetadata>
{
    private final DataAccess dataAccess;

    @Inject
    public ChangeMetadataFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ChangeMetadata create( Object o, MappingContext mappingContext )
    {
        ChangeBatch asChange = ( ChangeBatch ) o;
        if ( asChange.getKey() != null )
        {
            return dataAccess.find( ChangeMetadata.class, asChange.getKey() );
        }

        return new ChangeMetadata();
    }
}
