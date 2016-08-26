package org.ctoolkit.migration.agent.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISet
{
    void setKey(String key);

    void setName(String name);

    String getName();

    void setMapReduceJobId(String mapReduceJobId);

    void setCreateDate(Date createDate);

    void setUpdateDate(Date updateDate);

    List<? extends ISetItem> getItems();
}
