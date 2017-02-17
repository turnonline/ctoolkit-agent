package org.ctoolkit.migration.agent.model;

import java.util.Date;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISetItem
{
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

    byte[] getData();

    void setData( byte[] data );

    DataType getDataType();

    void setDataType( DataType dataType );

    long getDataLength();

    void setDataLength(long dataLength);

    void setKey( String key );

    String getKey();

    void setName( String name );

    String getName();

    void setFileName(String fileName);

    void setState( JobState state );

    JobState getState();

    void setCreateDate( Date createDate );

    void setUpdateDate( Date updateDate );

    void setError(String error);

    String getError();
}
