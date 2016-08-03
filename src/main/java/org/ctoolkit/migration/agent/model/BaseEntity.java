package org.ctoolkit.migration.agent.model;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.OnSave;

import java.util.Date;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class BaseEntity
{
    @Id
    private Long id;

    private Date createDate;

    private Date updateDate;

    private String createdBy;

    private String updatedBy;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
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

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy( String updatedBy )
    {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( !( o instanceof BaseEntity ) ) return false;

        BaseEntity that = ( BaseEntity ) o;

        return id != null ? id.equals( that.id ) : that.id == null;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Method is called before every update. If entity is updated first time
     * change <code>createDate</code>, else change <code>updateDate</code>
     */
    @OnSave
    private void onSave()
    {
        if ( createDate == null )
        {
            createDate = new Date();
            // TODO: created by
        }
        else
        {
            updateDate = new Date();
            // TODO: updated by
        }
    }

    @Override
    public String toString()
    {
        return "BaseEntity{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
