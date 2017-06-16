package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.repackaged.com.google.api.client.util.Base64;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.StringValue;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity decoder is used to decode changeset to Entity.Builder
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityDecoder
{
    private static Logger logger = LoggerFactory.getLogger( EntityDecoder.class );

    @Inject
    @ProjectId
    private String projectId;

    public void decode( Entity.Builder entityBuilder, String name, String type, String value )
    {
        if ( null == value )
        {
            entityBuilder.setNull( name );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_STRING.equals( type ) )
        {
            entityBuilder.set( name, value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE.equals( type ) )
        {
            entityBuilder.set( name, Double.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_FLOAT.equals( type ) )
        {
            entityBuilder.set( name, Float.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_INTEGER.equals( type ) )
        {
            entityBuilder.set( name, Integer.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LONG.equals( type ) )
        {
            entityBuilder.set( name, Long.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN.equals( type ) )
        {
            entityBuilder.set( name, Boolean.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DATE.equals( type ) )
        {
            entityBuilder.set( name, Timestamp.of( new Date( Long.valueOf( value ) ) ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BLOB.equals( type ) )
        {
            try
            {
                entityBuilder.set( name, Blob.copyFrom( Base64.decodeBase64( value ) ) );
            }
            catch ( Exception e )
            {
                logger.error( "Error by encoding blob: '" + value + "'" );
                entityBuilder.setNull( name );
            }
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_KEY.equals( type ) )
        {
            entityBuilder.set( name, parseKeyByIdOrName( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_LONG.equals( type ) )
        {
            List<LongValue> list = new ArrayList<>();

            for ( String s : value.split( "," ) )
            {
                try
                {
                    list.add( LongValue.of( Long.valueOf( s ) ) );
                }
                catch ( NumberFormatException e )
                {
                    logger.error( "Unable to convert value to long: '" + s + "'" );
                }
            }

            entityBuilder.set( name, list );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_STRING.equals( type ) )
        {
            List<StringValue> list = new ArrayList<>();

            for ( String s : value.split( "," ) )
            {
                list.add( StringValue.of( s ) );
            }

            entityBuilder.set( name, list );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_KEY.equals( type ) )
        {
            List<KeyValue> list = new ArrayList<>();

            for ( String fullKey : value.split( "," ) )
            {
                list.add( KeyValue.of( parseKeyByIdOrName( fullKey ) ) );
            }

            entityBuilder.set( name, list );
        }
        else
        {
            logger.error( "Unknown entity type '" + type + "'" );
        }
    }

    public Key parseKeyByIdOrName( String stringKey )
    {
        String[] split = stringKey.trim().split( "::" );

        String kind;
        String idName;
        Key parentKey = null;

        for ( String s : split )
        {
            String[] spl = s.split( ":" );
            kind = spl[0].trim();
            idName = spl[1].trim();

            if ( parentKey == null )
            {
                try
                {
                    parentKey = Key.newBuilder( projectId, kind, Long.valueOf( idName ) ).build();
                }
                catch ( NumberFormatException e )
                {
                    parentKey = Key.newBuilder( projectId, kind, idName ).build();
                }
            }
            else
            {
                try
                {
                    parentKey = Key.newBuilder( parentKey, kind, Long.valueOf( idName ) ).build();
                }
                catch ( NumberFormatException e )
                {
                    parentKey = Key.newBuilder( parentKey, kind, idName ).build();
                }
            }
        }

        return parentKey;
    }
}
