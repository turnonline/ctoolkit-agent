package org.ctoolkit.migration.agent.model;

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

    String getKey();

    void setKey( String key );

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
