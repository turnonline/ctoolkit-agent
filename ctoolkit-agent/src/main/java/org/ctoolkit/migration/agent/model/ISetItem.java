package org.ctoolkit.migration.agent.model;

import java.util.Date;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISetItem
{
    byte[] getXml();

    void setXml(byte[] xml);

    void setKey(String key);

    String getKey();

    void setName(String name);

    String getName();

    void setCreateDate(Date createDate);

    void setUpdateDate(Date updateDate);
}
