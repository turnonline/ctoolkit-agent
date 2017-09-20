package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.repackaged.com.google.api.client.util.Base64;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Datastore entity decoder
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityDecoder
{
    private static Logger logger = LoggerFactory.getLogger( EntityDecoder.class );

    @Inject
    @ProjectId
    private String projectId;

    public Value<?> decode( String type, String value )
    {
        if ( null == value )
        {
            return new NullValue();
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_STRING.equals( type ) )
        {
            return new ValueResolver()
            {
                @Override
                Value<?> toValue( String value )
                {
                    return new StringValue( value );
                }
            }.resolve( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE.equals( type ) )
        {
            return new DoubleValue( Double.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LONG.equals( type ) )
        {
            return new ValueResolver()
            {
                @Override
                Value<?> toValue( String value )
                {
                    return new LongValue( Long.valueOf( value ) );
                }
            }.resolve( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN.equals( type ) )
        {
            return new BooleanValue( Boolean.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DATE.equals( type ) )
        {
            return new TimestampValue( Timestamp.of( new Date( Long.valueOf( value ) ) ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BLOB.equals( type ) )
        {
            return new BlobValue( Blob.copyFrom( Base64.decodeBase64( value ) ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_REFERENCE.equals( type ) )
        {
            return new ValueResolver()
            {
                @Override
                Value<?> toValue( String value )
                {
                    return new KeyValue( parseKeyByIdOrName( value ) );
                }
            }.resolve( value );
        }
        else
        {
            logger.error( "Unknown entity type '" + type + "'" );
        }

        return new NullValue();
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

    private static abstract class ValueResolver
    {
        Value<?> resolve( String value )
        {
            if ( value.contains( "," ) )
            {
                List<Value<?>> list = new ArrayList<>();

                for ( String splitValue : value.split( "," ) )
                {
                    list.add( toValue( splitValue ) );
                }

                return new ListValue( list );
            }
            else
            {
                return toValue( value );
            }
        }

        abstract Value<?> toValue( String value );
    }
}
