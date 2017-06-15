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

import java.util.Date;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface ISetItem
{
    byte[] getData();

    void setData( byte[] data );

    DataType getDataType();

    void setDataType( DataType dataType );

    long getDataLength();

    void setDataLength( long dataLength );

    Long getId();

    void setId( Long id );

    String getName();

    void setName( String name );

    void setFileName( String fileName );

    JobState getState();

    void setState( JobState state );

    void setCreateDate( Date createDate );

    void setUpdateDate( Date updateDate );

    String getError();

    void setError( String error );

    enum DataType
    {
        XML( "application/xml" ),
        JSON( "application/json" );

        private String mimeType;

        DataType( String mimeType )
        {
            this.mimeType = mimeType;
        }

        public String mimeType()
        {
            return mimeType;
        }
    }
}
