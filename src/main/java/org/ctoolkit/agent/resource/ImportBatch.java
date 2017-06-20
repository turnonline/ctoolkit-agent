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
public class ImportBatch
        implements ISet<ImportJob>
{
    private Long id;

    private String name;

    private String jobId;

    private String token;

    private Date createDate;

    private Date updateDate;

    private ImportJob jobInfo;

    private List<ImportItem> items = new ArrayList<>();

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId( String jobId )
    {
        this.jobId = jobId;
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
    public ImportJob getJobInfo()
    {
        return jobInfo;
    }

    @Override
    public void setJobInfo( ImportJob jobInfo )
    {
        this.jobInfo = jobInfo;
    }

    public List<ImportItem> getItems()
    {
        return items;
    }

    public void setItems( List<ImportItem> items )
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "Import{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", jobId='" + jobId + '\'' +
                ", token='" + token + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", jobInfo=" + jobInfo +
                ", items=" + items +
                '}';
    }

    public static class ImportItem
            implements ISetItem
    {
        private Long id;

        private String name;

        private byte[] data;

        private String fileName;

        private DataType dataType;

        private long dataLength;

        private JobState state;

        private Date createDate;

        private Date updateDate;

        private String error;

        public Long getId()
        {
            return id;
        }

        public void setId( Long id )
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

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
                    "id='" + id + '\'' +
                    ", name=" + name +
                    ", data.length=" + ( data != null ? data.length : null ) +
                    ", dataType=" + dataType +
                    ", dataLength=" + dataLength +
                    ", fileName=" + fileName +
                    ", state=" + state +
                    ", createDate=" + createDate +
                    ", updateDate=" + updateDate +
                    ", error=" + error +
                    '}';
        }
    }
}
