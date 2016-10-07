package org.ctoolkit.migration.agent.model;

import java.util.Date;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISetItem
{
    enum DataType {
        XML,
        JSON
    }

    byte[] getData();

    void setData( byte[] data );

    DataType getDataType();

    void setDataType(DataType dataType);

    void setKey(String key);

    String getKey();

    void setName(String name);

    String getName();

    void setState(JobState state);

    JobState getState();

    void setCreateDate(Date createDate);

    void setUpdateDate(Date updateDate);
}
