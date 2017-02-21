package org.ctoolkit.migration.agent.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISet<JI extends JobInfo>
{
    void setKey( String key );

    String getName();

    void setName( String name );

    void setMapReduceJobId( String mapReduceJobId );

    void setCreateDate( Date createDate );

    void setUpdateDate( Date updateDate );

    JI getJobInfo();

    void setJobInfo( JI jobInfo );

    List<? extends ISetItem> getItems();
}
