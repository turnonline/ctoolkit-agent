package org.ctoolkit.migration.agent.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Import DTO for rest communication
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ExportBatch
        implements ISet
{
    private String key;

    private String name;

    private String mapReduceJobId;

    private Date createDate;

    private Date updateDate;

    private List<ExportItem> items = new ArrayList<>();

    public static class ExportItem
            implements ISetItem
    {
        private String key;

        private String name;

        private byte[] data;

        private DataType dataType;

        private String entityToExport;

        private Date createDate;

        private Date updateDate;

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

        public byte[] getData()
        {
            return data;
        }

        public void setData( byte[] data )
        {
            this.data = data;
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

        public String getEntityToExport()
        {
            return entityToExport;
        }

        public void setEntityToExport( String entityToExport )
        {
            this.entityToExport = entityToExport;
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
        public String toString()
        {
            return "Item{" +
                    "key='" + key + '\'' +
                    ", name=" + name +
                    ", data.length=" + ( data != null ? data.length : null) +
                    ", entityToExport='" + entityToExport + '\'' +
                    ", createDate=" + createDate +
                    ", updateDate=" + updateDate +
                    '}';
        }
    }

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

    public List<ExportItem> getItems()
    {
        return items;
    }

    public void setItems( List<ExportItem> items )
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "Export{" +
                "key='" + key + '\'' +
                ", name=" + name +
                ", mapReduceJobId='" + mapReduceJobId + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", items=" + items +
                '}';
    }
}
