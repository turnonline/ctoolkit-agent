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
public class BaseEntity
{
    private Long id;

    private Date createDate;

    private Date updateDate;

    private String createdBy;

    private String updatedBy;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( Date createDate )
    {
        this.createDate = createDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( Date updateDate )
    {
        this.updateDate = updateDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy( String updatedBy )
    {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( !( o instanceof BaseEntity ) ) return false;

        BaseEntity that = ( BaseEntity ) o;

        return id != null ? id.equals( that.id ) : that.id == null;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    public com.google.cloud.datastore.Key key()
    {
        throw new IllegalArgumentException( "Method key() must be implemented by super class!!!" );
    }

    /**
     * Method is called before every update. If entity is updated first time
     * change <code>createDate</code>, else change <code>updateDate</code>
     */
    private void onSave()
    {
        if ( createDate == null )
        {
            createDate = new Date();
            // TODO: created by
        }
        else
        {
            updateDate = new Date();
            // TODO: updated by
        }
    }

    @Override
    public String toString()
    {
        return "BaseEntity{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
