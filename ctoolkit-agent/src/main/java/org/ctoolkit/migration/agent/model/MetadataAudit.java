package org.ctoolkit.migration.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Entity( name = "_MetadataAudit" )
public class MetadataAudit
        extends BaseEntity
{
    private Action action;

    private Operation operation;

    private String ownerId;

    private String userPhotoUrl;

    private String userDisplayName;

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
        return "MetadataAudit{" +
                "action=" + action +
                ", operation=" + operation +
                ", ownerId='" + ownerId + '\'' +
                ", userPhotoUrl='" + userPhotoUrl + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                "} " + super.toString();
    }

    public enum Action
    {
        CREATE,
        UPDATE,
        DELETE,

        START_JOB,
        CANCEL_JOB,
        DELETE_JOB,

        MIGRATION
    }

    public enum Operation
    {
        IMPORT,
        IMPORT_ITEM,
        EXPORT,
        EXPORT_ITEM,
        CHANGE,
        CHANGE_ITEM
    }
}
