/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.api.client.util.Base64;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Datastore entity encoder
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class EntityEncoder
{
    private static final Logger logger = LoggerFactory.getLogger( EntityEncoder.class );

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
        else if ( value instanceof Blob )
        {
            return createBlobProperty( name, ( Blob ) value );
        }
        else if ( value instanceof Key )
        {
            return createKeyProperty( name, ( Key ) value );
        }
        else if ( value instanceof List )
        {
            return createListProperty( name, ( List ) value );
        }

        throw new IllegalArgumentException( "Unknown entity type '" + value.getClass().getName() + "'" );
    }

    /**
     * Transforms a type and a string value to real object with given type and value.
     *
     * @param type  the type of the property
     * @param value the string represented value of the property
     * @return ChangeSetEntityProperty representation of the property
     */
    @Deprecated
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
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_BLOB.equals( type ) )
        {
            try
            {
                return new Blob( Base64.decodeBase64( value ) );
            }
            catch ( Exception e )
            {
                logger.error( "Error by encoding blob: '" + value + "'" );
                return null;
            }
        }
        else if ( ChangeSetEntityProperty.PROPERTY_TYPE_KEY.equals( type ) )
        {
            return parseKeyByIdOrName( value );
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

    public String formatKey( Key key )
    {
        List<Key> parentKeys = new ArrayList<>();
        while ( key != null )
        {
            parentKeys.add( key );
            key = key.getParent();
        }

        Collections.reverse( parentKeys );
        StringBuilder formattedKey = new StringBuilder();

        for ( Key parentKey : parentKeys )
        {
            if ( formattedKey.length() > 0 )
            {
                formattedKey.append( "::" );
            }

            formattedKey.append( parentKey.getKind() );
            formattedKey.append( ":" );
            formattedKey.append( parentKey.getName() != null ? parentKey.getName() : parentKey.getId() );
        }

        return formattedKey.length() > 0 ? formattedKey.toString() : null;
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
     * Renders a Blob type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createBlobProperty( String name, Blob object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BLOB,
                Base64.encodeBase64String( object.getBytes() ) );
    }

    /**
     * Renders a Key type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createKeyProperty( String name, Key object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_KEY, formatKey( object ) );
    }

    /**
     * Renders a List type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createListProperty( String name, List object )
    {
        String propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_NULL;
        StringBuilder value = new StringBuilder();

        for ( Object o : object )
        {
            if ( value.length() > 0 )
            {
                value.append( "," );
            }

            if ( o instanceof String )
            {
                value.append( o );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_STRING;
            }
            else if ( o instanceof Key )
            {
                value.append( formatKey( ( Key ) o ) );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_KEY;
            }
            else
            {
                throw new IllegalArgumentException( "Unknown list type: " + o.getClass().getName() );
            }
        }

        return new ChangeSetEntityProperty( name, propertyType, value.toString() );
    }
}
