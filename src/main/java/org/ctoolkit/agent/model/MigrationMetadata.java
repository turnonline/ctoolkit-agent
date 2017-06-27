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

package org.ctoolkit.agent.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import org.ctoolkit.agent.annotation.EntityMarker;

/**
 * Migration metadata entity.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@EntityMarker( name = "_MigrationMetadata" )
public class MigrationMetadata
        extends BaseMetadata<MigrationMetadataItem>
{
    private String source;

    private String target;

    @Override
    protected MigrationMetadataItem newItem()
    {
        return new MigrationMetadataItem( this );
    }

    @Override
    protected Class<MigrationMetadataItem> itemClass()
    {
        return MigrationMetadataItem.class;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource( String source )
    {
        this.source = source;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget( String target )
    {
        this.target = target;
    }

    public boolean isSourceSameAsTarget()
    {
        return source.equals( target );
    }

    @Override
    public void convert( Entity entity )
    {
        super.convert( entity );

        setSource( entity.contains( "source" ) ? entity.getString( "source" ) : null );
        setTarget( entity.contains( "target" ) ? entity.getString( "target" ) : null );
    }

    @Override
    protected void saveAdditional( FullEntity.Builder<IncompleteKey> builder )
    {
        super.saveAdditional( builder );

        if ( getSource() != null )
        {
            builder.set( "source", getSource() );
        }
        if ( getTarget() != null )
        {
            builder.set( "target", getTarget() );
        }
    }

    @Override
    public String toString()
    {
        return "MigrationMetadata{" +
                "} " + super.toString();
    }
}
