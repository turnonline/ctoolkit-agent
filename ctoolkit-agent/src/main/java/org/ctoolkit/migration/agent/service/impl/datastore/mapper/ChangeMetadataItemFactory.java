package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.ctoolkit.migration.agent.model.ChangeBatch;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;
import org.ctoolkit.migration.agent.service.DataAccess;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
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
