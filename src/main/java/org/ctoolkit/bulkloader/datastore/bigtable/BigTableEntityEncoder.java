package org.ctoolkit.bulkloader.datastore.bigtable;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;
import com.google.common.io.CharStreams;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntityProperty;
import org.ctoolkit.bulkloader.common.EntityEncoderException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * AppEngine implementation of the Entity encoder
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BigTableEntityEncoder
{

    /**
     * Logger for this class
     */
    private final Logger logger = Logger.getLogger( BigTableEntityEncoder.class.getName() );

    /**
     * Method converts change set entity to data store entity
     *
     * @param csEntity entity to encode
     * @return encoded entity
     */
    public Entity toDataStoreEntity( final ChangeSetEntity csEntity )
    {
        // create the main entity

        // 1. the kind has to be specified
        if ( null == csEntity.getKind() )
        {
            logger.severe( "Missing entity kind! It has to be specified" );
            return null;
        }

        // cannot be both id and name specified
        // TODO: validation: exception, or take id as higher priority?

        // generate parent key
        Key parentKey = null;
        // parentEntityId has top priority
        if ( null != csEntity.getParentKey() )
        {
            parentKey = parseKeyByIdOrName( csEntity.getParentKey() );
            // parent kind/id
        }
        else if ( null != csEntity.getParentId() && null != csEntity.getParentKind() )
        {
            parentKey = KeyFactory.createKey( csEntity.getParentKind(), csEntity.getParentId() );
            // parent kind/name has the lowest priority in the reference chain
        }
        else if ( null != csEntity.getParentName() && null != csEntity.getParentKind() )
        {
            parentKey = KeyFactory.createKey( csEntity.getParentKind(), csEntity.getParentName() );
        }

        // generate the entity
        Entity dsEntity;
        // look for a key property
        if ( null != csEntity.getKey() )
        {
            // ignore parent key, because it has to be composed within the entity key
            dsEntity = new Entity( KeyFactory.stringToKey( csEntity.getKey() ) );
        }
        else if ( null != csEntity.getId() )
        {
            // check if there is id set
            // look for parent kind/id
            if ( null != parentKey )
            {
                // build the entity key
                Key key = new KeyFactory.Builder( parentKey ).addChild( csEntity.getKind(), csEntity.getId() ).getKey();
                dsEntity = new Entity( key );
            }
            else
            {
                dsEntity = new Entity( KeyFactory.createKey( csEntity.getKind(), csEntity.getId() ) );
            }
        }
        else if ( null != csEntity.getName() )
        {
            // build the entity key
            if ( null != parentKey )
            {
                dsEntity = new Entity( csEntity.getKind(), csEntity.getName(), parentKey );
            }
            else
            {
                dsEntity = new Entity( csEntity.getKind(), csEntity.getName() );
            }
        }
        else
        {
            dsEntity = new Entity( csEntity.getKind() );
        }

        // set up the properties
        if ( csEntity.hasProperties() )
        {
            for ( ChangeSetEntityProperty prop : csEntity.getProperties() )
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
     * Method encodes a data store entity into change set entity representation
     *
     * @param dataStoreEntity entity to encode
     * @return encoded entity
     */
    public ChangeSetEntity toChangeSetEntity( final Entity dataStoreEntity )
    {
        // create main entity
        ChangeSetEntity csEntity = ChangeSetEntity.createChangeSetEntity();
        csEntity.setKind( dataStoreEntity.getKind() );
        csEntity.setKey( KeyFactory.keyToString( dataStoreEntity.getKey() ) );

        // set up entity properties
        Iterator<Entry<String, Object>> it = dataStoreEntity.getProperties().entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String, Object> pairs = it.next();
            ChangeSetEntityProperty property = encodeProperty( pairs.getKey(), pairs.getValue() );
            csEntity.addProperty( property );
        }
        return csEntity;
    }

    /**
     * Method encodes an entity property into a ChangeSetEntityProperty
     *
     * @param name  name of the property
     * @param value value of the property
     * @return ChangeSetEntityProperty representation of the property
     * @throws EntityEncoderException
     */
    public ChangeSetEntityProperty encodeProperty( final String name, final Object value )
    {
        if ( null == value )
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

        logger.severe( "Unknown entity type '" + value.getClass().getName() + "'" );
        return null;
    }

    /**
     * Method transforms a type and a string value to real object with given
     * type and value
     *
     * @param type  type of the property
     * @param value string represented value of the property
     * @return ChangeSetEntityProperty representation of the property
     * @throws EntityEncoderException
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
                logger.severe( "Error by encoding short blob: '" + value + "'" );
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
                logger.severe( "Error by encoding blob: '" + value + "'" );
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
                logger.severe( "Error by encoding text: '" + value + "'" );
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
                    logger.severe( "Unable to convert value to long: '" + s + "'" );
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

        logger.severe( "Unknown entity type '" + type + "'" );
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
     * Method renders a String type property to String
     *
     * @param name name of the property
     */
    private ChangeSetEntityProperty createNullProperty( String name )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_NULL, null );
    }

    /**
     * Method renders a String type property to String
     *
     * @param name   name of the property
     * @param object
     */
    private ChangeSetEntityProperty createStringProperty( String name, String object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_STRING, object );
    }

    /**
     * Method renders a Double type property to String
     *
     * @param name  name of the property
     * @param value object to render
     */
    private ChangeSetEntityProperty createDoubleProperty( String name, Double value )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE, value.toString() );
    }

    /**
     * Method renders a Float type property to String
     *
     * @param name   name of the property
     * @param object object to render
     */
    private ChangeSetEntityProperty createFloatProperty( String name, Float object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_FLOAT, object.toString() );
    }

    /**
     * Method renders a Integer type property to String
     *
     * @param name   name of the property
     * @param object object to render
     */
    private ChangeSetEntityProperty createIntegerProperty( String name, Integer object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_INTEGER, object.toString() );
    }

    /**
     * Method renders a Integer type property to String
     *
     * @param name   name of the property
     * @param object object to render
     */
    private ChangeSetEntityProperty createLongProperty( String name, Long object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_LONG, object.toString() );
    }

    /**
     * Method renders a Date type property to String
     *
     * @param name name of the property
     * @param date object to render
     */
    private ChangeSetEntityProperty createDateProperty( String name, Date date )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_DATE, Long.valueOf( date.getTime() ).toString() );
    }

    /**
     * Method renders a Boolean type property to String
     *
     * @param name   name of the property
     * @param object object to render
     * @throws XMLStreamException
     */
    private ChangeSetEntityProperty createBooleanProperty( String name, Boolean object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN, object.toString() );
    }

    /**
     * Method renders a ShortBlob type property to String
     *
     * @param name   name of the property
     * @param object object to render
     */
    private ChangeSetEntityProperty createShortBlobProperty( String name, ShortBlob object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_SHORTBLOB, Base64.encode( object.getBytes() ) );
    }

    /**
     * Method renders a Blob type property to String
     *
     * @param name   name of the property
     * @param object object to render
     */
    private ChangeSetEntityProperty createBlobProperty( String name, Blob object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BLOB, Base64.encode( object.getBytes() ) );
    }
}
