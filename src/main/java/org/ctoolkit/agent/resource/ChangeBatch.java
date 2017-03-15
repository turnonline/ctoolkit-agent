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

package org.ctoolkit.agent.resource;

import org.ctoolkit.agent.model.ISet;
import org.ctoolkit.agent.model.ISetItem;
import org.ctoolkit.agent.model.JobState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Import DTO for rest communication
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeBatch
        implements ISet<ChangeJob>
{
    private String key;

    private String name;

    private String mapReduceJobId;

    private String token;

    private Date createDate;

    private Date updateDate;

    private ChangeJob jobInfo;

    private List<ChangeItem> items = new ArrayList<>();

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    public String getMapReduceJobId()
    {
        return mapReduceJobId;
    }

    public void setMapReduceJobId( String mapReduceJobId )
    {
        this.mapReduceJobId = mapReduceJobId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken( String token )
    {
        this.token = token;
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

    @Override
    public ChangeJob getJobInfo()
    {
        return jobInfo;
    }

    @Override
    public void setJobInfo( ChangeJob jobInfo )
    {
        this.jobInfo = ( ChangeJob ) jobInfo;
    }

    public List<ChangeItem> getItems()
    {
        return items;
    }

    public void setItems( List<ChangeItem> items )
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "Change{" +
                "key='" + key + '\'' +
                ", name=" + name +
                ", mapReduceJobId='" + mapReduceJobId + '\'' +
                ", token='" + token + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", jobInfo=" + jobInfo +
                ", items=" + items +
                '}';
    }

    public static class ChangeItem
            implements ISetItem
    {
        private String key;

        private String name;

        private byte[] data;

        private String fileName;

        private DataType dataType;

        private long dataLength;

        private JobState state;

        private Date createDate;

        private Date updateDate;

        private String error;

        public String getKey()
        {
            return key;
        }

        public void setKey( String key )
        {
            this.key = key;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public void setName( String name )
        {
            this.name = name;
        }

        @Override
        public JobState getState()
        {
            return state;
        }

        @Override
        public void setState( JobState state )
        {
            this.state = state;
        }

        public byte[] getData()
        {
            return data;
        }

        public void setData( byte[] data )
        {
            this.data = data;
        }

        public String getFileName()
        {
            return fileName;
        }

        @Override
        public void setFileName( String fileName )
        {
            this.fileName = fileName;
        }

        @Override
        public DataType getDataType()
        {
            return dataType;
        }

        @Override
        public void setDataType( DataType dataType )
        {
            this.dataType = dataType;
        }

        @Override
        public long getDataLength()
        {
            return dataLength;
        }

        @Override
        public void setDataLength( long dataLength )
        {
            this.dataLength = dataLength;
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

        @Override
        public String getError()
        {
            return error;
        }

        @Override
        public void setError( String error )
        {
            this.error = error;
        }

        @Override
        public String toString()
        {
            return "Item{" +
                    "key='" + key + '\'' +
                    ", name=" + name +
                    ", data.length=" + ( data != null ? data.length : null ) +
                    ", fileName=" + fileName +
                    ", dataType=" + dataType +
                    ", dataLength=" + dataLength +
                    ", state=" + state +
                    ", createDate=" + createDate +
                    ", updateDate=" + updateDate +
                    ", error=" + error +
                    '}';
        }
    }
}
