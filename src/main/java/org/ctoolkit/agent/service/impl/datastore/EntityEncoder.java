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

import com.google.api.client.util.Base64;
import com.google.cloud.Timestamp;
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
import com.google.common.annotations.VisibleForTesting;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;

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
    /**
     * Encodes an entity property into a ChangeSetEntityProperty.
     *
     * @param name  the name of the property
     * @param value the value of the property
     * @return ChangeSetEntityProperty representation of the property
     */
    public ChangeSetEntityProperty encode( final String name, final Value<?> value )
    {
        if ( value == null || value instanceof NullValue )
        {
            return createNullProperty( name );
        }
        else if ( value instanceof StringValue )
        {
            return createStringProperty( name, ( ( StringValue ) value ).get() );
        }
        else if ( value instanceof LongValue )
        {
            return createLongProperty( name, ( ( LongValue ) value ).get() );
        }
        else if ( value instanceof DoubleValue )
        {
            return createDoubleProperty( name, ( ( DoubleValue ) value ).get() );
        }
        else if ( value instanceof BooleanValue )
        {
            return createBooleanProperty( name, ( ( BooleanValue ) value ).get() );
        }
        else if ( value instanceof TimestampValue )
        {
            Timestamp timestamp = ( ( TimestampValue ) value ).get();
            return createDateProperty( name, new Date( timestamp.toSqlTimestamp().getTime() ) );
        }
        else if ( value instanceof BlobValue )
        {
            return createBlobProperty( name, ( ( BlobValue ) value ).get().toByteArray() );
        }
        else if ( value instanceof KeyValue )
        {
            return createKeyProperty( name, ( ( KeyValue ) value ).get() );
        }
        else if ( value instanceof ListValue )
        {
            return createListProperty( name, ( ( ListValue ) value ).get() );
        }

        throw new IllegalArgumentException( "Unknown entity type '" + value.getClass().getName() + "'" );
    }

    @VisibleForTesting
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
    private ChangeSetEntityProperty createBlobProperty( String name, byte[] object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_BLOB,
                Base64.encodeBase64String( object ) );
    }

    /**
     * Renders a Key type property to String.
     *
     * @param name   the name of the property
     * @param object the object to render
     */
    private ChangeSetEntityProperty createKeyProperty( String name, Key object )
    {
        return new ChangeSetEntityProperty( name, ChangeSetEntityProperty.PROPERTY_TYPE_REFERENCE, formatKey( object ) );
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

            if ( o instanceof StringValue )
            {
                value.append( ( ( StringValue ) o ).get() );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_STRING;
            }
            else if ( o instanceof LongValue )
            {
                value.append( ( ( LongValue ) o ).get() );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_LONG;
            }
            else if ( o instanceof DoubleValue )
            {
                value.append( ( ( DoubleValue ) o ).get() );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE;
            }
            else if ( o instanceof BooleanValue )
            {
                value.append( ( ( BooleanValue ) o ).get() );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN;
            }
            else if ( o instanceof TimestampValue )
            {
                value.append( ( ( TimestampValue ) o ).get().toSqlTimestamp().getTime() );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_DATE;
            }
            else if ( o instanceof BlobValue )
            {
                value.append( Base64.encodeBase64String( ( ( BlobValue ) o ).get().toByteArray() ) );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_BLOB;
            }
            else if ( o instanceof KeyValue )
            {
                value.append( formatKey( ( ( KeyValue ) o ).get() ) );
                propertyType = ChangeSetEntityProperty.PROPERTY_TYPE_REFERENCE;
            }
            else
            {
                throw new IllegalArgumentException( "Unknown list type: " + o.getClass().getName() );
            }
        }

        return new ChangeSetEntityProperty( name,
                propertyType,
                ChangeSetEntityProperty.PROPERTY_MULTIPLICITY_LIST,
                value.toString()
        );
    }
}
