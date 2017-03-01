package org.ctoolkit.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.agent.model.ChangeBatch;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeMetadataItemFactory
        implements ObjectFactory<ChangeMetadataItem>
{
    private final DataAccess dataAccess;

    @Inject
    public ChangeMetadataItemFactory( DataAccess dataAccess )
    {
        this.dataAccess = dataAccess;
    }

    @Override
    public ChangeMetadataItem create( Object o, MappingContext mappingContext )
    {
        ChangeBatch.ChangeItem asChangeItem = ( ChangeBatch.ChangeItem ) o;
        if ( asChangeItem.getKey() != null )
        {
            return dataAccess.find( ChangeMetadataItem.class, asChangeItem.getKey() );
        }

        String metadataId = ( String ) mappingContext.getProperty( "metadataId" );
        ChangeMetadata changeMetadata = dataAccess.find( ChangeMetadata.class, metadataId );
        return new ChangeMetadataItem( changeMetadata );
    }
}
