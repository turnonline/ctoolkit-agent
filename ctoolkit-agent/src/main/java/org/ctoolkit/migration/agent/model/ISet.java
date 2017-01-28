package org.ctoolkit.migration.agent.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ISet<JI extends JobInfo>
{
    void setKey( String key );

    void setName( String name );

    String getName();

    void setMapReduceJobId( String mapReduceJobId );

    void setCreateDate( Date createDate );

    void setUpdateDate( Date updateDate );

    void setJobInfo( JI jobInfo );

    JI getJobInfo();

    List<? extends ISetItem> getItems();
}
