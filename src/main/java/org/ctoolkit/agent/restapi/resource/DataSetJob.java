package org.ctoolkit.agent.restapi.resource;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * The basic resource to model operations on data set.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Entity
abstract class DataSetJob
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Long dataSetId;

    private boolean completed;

    private Date completedAt;

    public DataSetJob()
    {
    }

    public DataSetJob( Long id )
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public Long getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( Long dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted( boolean completed )
    {
        this.completed = completed;
    }

    public Date getCompletedAt()
    {
        return completedAt;
    }

    public void setCompletedAt( Date completedAt )
    {
        this.completedAt = completedAt;
    }
}
