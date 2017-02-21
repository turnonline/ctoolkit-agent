package org.ctoolkit.migration.agent.service.impl.datastore.mapper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.ctoolkit.migration.agent.service.impl.datastore.EntityEncoder;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetEntity;
import org.ctoolkit.migration.agent.shared.resources.ChangeSetEntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

/**
 * Mapper for {@link ChangeSetEntity} to {@link Entity} model beans
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeSetEntityToEntityMapper
        extends CustomMapper<ChangeSetEntity, Entity>
{
    private final EntityEncoder encoder;

    private Logger logger = LoggerFactory.getLogger( ChangeSetEntityToEntityMapper.class );

    @Inject
    public ChangeSetEntityToEntityMapper( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public void mapAtoB( ChangeSetEntity changeSetEntity, Entity entity, MappingContext context )
    {
        // the kind has to be specified
        if ( changeSetEntity.getKind() == null )
        {
            logger.error( "Missing entity kind! It has to be specified" );
            return;
        }

        // changeSetEntity up the properties
        if ( changeSetEntity.hasProperties() )
        {
            for ( ChangeSetEntityProperty prop : changeSetEntity.getProperty() )
            {
                if ( null == prop.getValue() )
                {
                    entity.setProperty( prop.getName(), encoder.decodeProperty( prop.getType(), null ) );
                }
                else
                {
                    entity.setProperty( prop.getName(), encoder.decodeProperty( prop.getType(), prop.getValue() ) );
                }
            }
        }
    }

    @Override
    public void mapBtoA( Entity entity, ChangeSetEntity changeSetEntity, MappingContext context )
    {
        // create main entity
        changeSetEntity.setKind( entity.getKind() );
        changeSetEntity.setKey( KeyFactory.keyToString( entity.getKey() ) );

        // changeSetEntity up entity properties
        for ( Map.Entry<String, Object> pairs : entity.getProperties().entrySet() )
        {
            ChangeSetEntityProperty property = encoder.encodeProperty( pairs.getKey(), pairs.getValue() );
            changeSetEntity.getProperty().add( property );
        }
    }
}
