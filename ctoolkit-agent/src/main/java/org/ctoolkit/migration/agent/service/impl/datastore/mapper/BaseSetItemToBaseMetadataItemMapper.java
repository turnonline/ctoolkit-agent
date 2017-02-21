package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.migration.agent.model.BaseMetadataItem;
import org.ctoolkit.migration.agent.model.ISetItem;

/**
 * Mapper for frontend to backend metadata item model beans
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class BaseSetItemToBaseMetadataItemMapper<FI extends ISetItem, BI extends BaseMetadataItem>
        extends CustomMapper<FI, BI>
{
    @Override
    @SuppressWarnings( "unchecked" )
    public void mapAtoB( FI setItem, BI metadataItem, MappingContext context )
    {
        metadataItem.setName( setItem.getName() );
        metadataItem.setData( setItem.getData() );
        metadataItem.setDataType( setItem.getDataType() );
    }

    @Override
    public void mapBtoA( BI metadataItem, FI setItem, MappingContext context )
    {
        setItem.setKey( metadataItem.getKey() );
        setItem.setName( metadataItem.getName() );
        setItem.setCreateDate( metadataItem.getCreateDate() );
        setItem.setUpdateDate( metadataItem.getUpdateDate() );
        setItem.setData( metadataItem.getData() );
        setItem.setFileName( metadataItem.getFileName() );
        setItem.setState( metadataItem.getState() );
        setItem.setDataType( metadataItem.getDataType() );
        setItem.setDataLength( metadataItem.getDataLength() );
        setItem.setError( metadataItem.getError() );
    }
}
