package org.ctoolkit.agent.datastore.bigtable;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;
import com.google.common.io.CharStreams;
import org.ctoolkit.agent.dataset.ChangeSetEntity;
import org.ctoolkit.agent.dataset.ChangeSetEntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

/**
 * The AppEngine implementation of the entity properties encoder.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class BigTableEntityEncoder
{
    private static final Logger logger = LoggerFactory.getLogger( BigTableEntityEncoder.class );

    /**
     * Converts the change set entity to the data store entity.
     *
     * @param set the entity to encode
     * @return the encoded entity
     */
    public Entity toDataStoreEntity( ChangeSetEntity set )
    {
        // create the main entity

        // 1. the kind has to be specified
        if ( set.getKind() == null )
        {
            logger.error( "Missing entity kind! It has to be specified" );
            return null;
        }

        // cannot be both id and name specified
        // TODO: validation: exception, or take id as higher priority?

        // generate parent key
        Key parentKey = null;

        // parentEntityId has top priority
        if ( set.getParentKey() != null )
        {
            parentKey = parseKeyByIdOrName( set.getParentKey() );
            // parent kind/id
        }
        else if ( set.getParentId() != null && set.getParentKind() != null )
        {
            parentKey = KeyFactory.createKey( set.getParentKind(), set.getParentId() );
            // parent kind/name has the lowest priority in the reference chain
        }
        else if ( set.getParentName() != null && set.getParentKind() != null )
        {
            parentKey = KeyFactory.createKey( set.getParentKind(), set.getParentName() );
        }

        // generate the entity
        Entity dsEntity;

        // look for a key property
        if ( set.getKey() != null )
        {
            // ignore parent key, because it has to be composed within the entity key
            dsEntity = new Entity( KeyFactory.stringToKey( set.getKey() ) );
        }
        else if ( set.getId() != null )
        {
            // check if there is id set
            // look for parent kind/id
            if ( parentKey != null )
            {
                // build the entity key
                Key key = new KeyFactory.Builder( parentKey ).addChild( set.getKind(), set.getId() ).getKey();
                dsEntity = new Entity( key );
            }
            else
            {
                dsEntity = new Entity( KeyFactory.createKey( set.getKind(), set.getId() ) );
            }
        }
        else if ( set.getName() != null )
        {
            // build the entity key
            if ( parentKey != null )
            {
                dsEntity = new Entity( set.getKind(), set.getName(), parentKey );
            }
            else
            {
                dsEntity = new Entity( set.getKind(), set.getName() );
            }
        }
        else
        {
            dsEntity = new Entity( set.getKind() );
        }

        // set up the properties
        if ( set.hasProperties() )
        {
            for ( ChangeSetEntityProperty prop : set.getProperties() )
            {
                if ( null == prop.getValue() )
                {
                    dsEntity.setProperty( prop.getName(), decodeProperty( prop.getType(), null ) );
                }
                else
                {
                    dsEntity.setProperty( prop.getName(), decodeProperty( prop.getType(), prop.getValue() ) );
                }
            }
        }
        return dsEntity;
    }


    /**
     * Encodes a data store entity into change set entity representation.
     *
     * @param dataStoreEntity the entity to encode
     * @return the encoded entity
     */
    public ChangeSetEntity toChangeSetEntity( final Entity dataStoreEntity )
    {
        // create main entity
        ChangeSetEntity csEntity = ChangeSetEntity.createChangeSetEntity();
        csEntity.setKind( dataStoreEntity.getKind() );
        csEntity.setKey( KeyFactory.keyToString( dataStoreEntity.getKey() ) );

        // set up entity properties
        for ( Entry<String, Object> pairs : dataStoreEntity.getProperties().entrySet() )
        {
            ChangeSetEntityProperty property = encodeProperty( pairs.getKey(), pairs.getValue() );
            csEntity.addProperty( property );
        }
        return csEntity;
    }

    /**
     * Encodes an entity property into a ChangeSetEntityProperty.
     *
     * @param name  the name of the property
     * @param value the value of the property
     * @return ChangeSetEntityProperty representation of the property
     */
    public ChangeSetEntityProperty encodeProperty( final String name, final Object value )
    {
        if ( value == null )
        {
            return createNullProperty( name );
        }
        else if ( value instanceof String )
        {
            return createStringProperty( name, ( String ) value );
        }
        else if ( value instanceof Double )
        {
            return createDoubleProperty( name, ( Double ) value );
        }
        else if ( value instanceof Float )
        {
            return createFloatProperty( name, ( Float ) value );
        }
        else if ( value instanceof Integer )
        {
            return createIntegerProperty( name, ( Integer ) value );
        }
        else if ( value instanceof Long )
        {
            return createLongProperty( name, ( Long ) value );
        }
        else if ( value instanceof Boolean )
        {
            return createBooleanProperty( name, ( Boolean ) value );
        }
        else if ( value instanceof Date )
        {
            return createDateProperty( name, ( Date ) value );
        }
        else if ( value instanceof ShortBlob )
        {
            return createShortBlobProperty( name, ( ShortBlob ) value );
        }
        else if ( value instanceof Blob )
        {
            return createBlobProperty( name, ( Blob ) value );
        }

        logger.error( "Unknown entity type '" + value.getClass().getName() + "'" );
        return null;
    }

    /**
     * Transforms a type and a string value to real object with given type and value.
     *
     * @param type  the type of the property
     * @param value the string represented value of the property
     * @return ChangeSetEntityProperty representation of the property
     */
    public Object decodeProperty( String type, String value )
    {
        if ( value == null )
        {
            return null;
        }
        if ( ChangeSetEntityProperty.PROPERTY_TYPE_STRING.equals( type ) )
        {
            return value;
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE.equals( type ) )
        {
            return Double.valueOf( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_FLOAT.equals( type ) )
        {
            return Float.valueOf( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_INTEGER.equals( type ) )
        {
            return Integer.valueOf( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN.equals( type ) )
        {
            return Boolean.valueOf( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_DATE.equals( type ) )
        {
            return new Date( Long.valueOf( value ) );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LONG.equals( type ) )
        {
            return Long.valueOf( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_SHORTBLOB.equals( type ) )
        {
            try
            {
                return new ShortBlob( Base64.decode( value ) );
            }
            catch ( Base64DecoderException e )
            {
                logger.error( "Error by encoding short blob: '" + value + "'" );
                return null;
            }
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BLOB.equals( type ) )
        {
            try
            {
                return new Blob( Base64.decode( value ) );
            }
            catch ( Base64DecoderException e )
            {
                logger.error( "Error by encoding blob: '" + value + "'" );
                return null;
            }
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_KEY.equals( type ) )
        {
            return parseKeyByIdOrName( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_KEY_NAME.equals( type ) )
        {
            return parseKeyNames( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_TEXT.equals( type ) )
        {
            return new Text( value );
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_EXTERNAL_TEXT.equals( type ) )
        {
            try
            {
                InputStream stream = BigTableEntityEncoder.class.getResourceAsStream( value );
                InputStreamReader reader = new InputStreamReader( stream );

                return new Text( CharStreams.toString( reader ) );
            }
            catch ( IOException e )
            {
                logger.error( "Error by encoding text: '" + value + "'" );
                return null;
            }
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_LONG.equals( type ) )
        {
            List<Long> list = new ArrayList<>();
            for ( String s : value.split( "," ) )
            {
                try
                {
                    list.add( Long.valueOf( s ) );
                }
                catch ( NumberFormatException e )
                {
                    logger.error( "Unable to convert value to long: '" + s + "'" );
                }
            }

            return list;
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_ENUM.equals( type ) )
        {
            List<String> list = new ArrayList<>();
            Collections.addAll( list, value.split( "," ) );

            return list;
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_STRING.equals( type ) )
        {
            List<String> list = new ArrayList<>();
            Collections.addAll( list, value.split( "," ) );

            return list;
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_LIST_KEY.equals( type ) )
        {
            List<Key> list = new ArrayList<>();

            for ( String fullKey : value.split( "," ) )
            {
                list.add( parseKeyByIdOrName( fullKey ) );
            }

            return list;
        }

        logger.error( "Unknown entity type '" + type + "'" );
        return null;
    }

    /**
     * Parse given string to Key first try as Long Id then if parsing fails as String name.
     *
     * @param stringKey the input string key to parse
     * @return the parsed key
     */
    private Key parseKeyByIdOrName( String stringKey )
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
                    parentKey = KeyFactory.createKey( kind, Long.parseLong( idName ) );
                }
                catch ( NumberFormatException e )
                {
                    parentKey = KeyFactory.createKey( kind, idName );
                }
            }
            else
            {
                try
                {
                    parentKey = KeyFactory.createKey( parentKey, kind, Long.parseLong( idName ) );
                }
                catch ( NumberFormatException e )
                {
                    parentKey = KeyFactory.createKey( parentKey, kind, idName );
                }
            }
        }

        return parentKey;
    }

    private Key parseKeyNames( String stringKey )
    {
        String[] split = stringKey.trim().split( "::" );

        String kind;
        String name;
        Key parentKey = null;

        for ( String s : split )
        {
            String[] spl = s.split( ":" );
            kind = spl[0].trim();
            name = spl[1].trim();

            if ( parentKey == null )
            {
                parentKey = KeyFactory.createKey( kind, name );
            }
            else
            {
                parentKey = KeyFactory.createKey( parentKey, kind, name );
            }
        }

        return parentKey;
    }

    /**
     * Renders a String type property to String.
     *
     * @param name name of the property
     */
    private ChangeSetEntityProperty createNullProperty( String name )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_NULL, null );
    }

    /**
     * Renders a String type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createStringProperty( String name, String object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_STRING, object );
    }

    /**
     * Renders a Double type property to String.
     *
     * @param name  the name of the property
     * @param value the object to render
     */
    private ChangeSetEntityProperty createDoubleProperty( String name, Double value )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE, value.toString() );
    }

    /**
     * Renders a Float type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createFloatProperty( String name, Float object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_FLOAT, object.toString() );
    }

    /**
     * Renders a Integer type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createIntegerProperty( String name, Integer object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_INTEGER, object.toString() );
    }

    /**
     * Renders a Integer type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createLongProperty( String name, Long object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_LONG, object.toString() );
    }

    /**
     * Renders a Date type property to String.
     *
     * @param name the name of the property
     * @param date the object to render
     */
    private ChangeSetEntityProperty createDateProperty( String name, Date date )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_DATE,
                Long.valueOf( date.getTime() ).toString() );
    }

    /**
     * Renders a Boolean type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createBooleanProperty( String name, Boolean object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN, object.toString() );
    }

    /**
     * Renders a ShortBlob type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createShortBlobProperty( String name, ShortBlob object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_SHORTBLOB,
                Base64.encode( object.getBytes() ) );
    }

    /**
     * Renders a Blob type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createBlobProperty( String name, Blob object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BLOB,
                Base64.encode( object.getBytes() ) );
    }
}
