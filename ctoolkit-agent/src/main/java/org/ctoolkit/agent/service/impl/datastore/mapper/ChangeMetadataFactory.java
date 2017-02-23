package org.ctoolkit.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.model.ChangeBatch;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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
