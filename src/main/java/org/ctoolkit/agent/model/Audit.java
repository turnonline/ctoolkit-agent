package org.ctoolkit.agent.model;

import java.util.Date;

/**
 * Audit
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Audit
{
    private Date createDate;

    private String createdBy;

    private Action action;

    private Operation operation;

    private String ownerId;

    private String userPhotoUrl;

    private String userDisplayName;

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( Date createDate )
    {
        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction( Action action )
    {
        this.action = action;
    }

    public Operation getOperation()
    {
        return operation;
    }

    public void setOperation( Operation operation )
    {
        this.operation = operation;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId( String ownerId )
    {
        this.ownerId = ownerId;
    }

    public String getUserPhotoUrl()
    {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl( String userPhotoUrl )
    {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserDisplayName()
    {
        return userDisplayName;
    }

    public void setUserDisplayName( String userDisplayName )
    {
        this.userDisplayName = userDisplayName;
    }

    @Override
    public String toString()
    {
        return "Audit{" +
                "createDate=" + createDate +
                ", createdBy='" + createdBy + '\'' +
                ", action=" + action +
                ", operation=" + operation +
                ", ownerId='" + ownerId + '\'' +
                ", userPhotoUrl='" + userPhotoUrl + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                '}';
    }
}
