package org.ctoolkit.migration.agent.service.impl;

import org.ctoolkit.migration.agent.service.RestContext;

/**
 * Request scoped rest context implementation. Store info about rest
 * metadata in thread local variable.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RestContextThreadLocal implements RestContext
{
    private String userId;

    private String userEmail;

    private String displayName;

    private String photoUrl;

    @Override
    public String getUserId()
    {
        return userId;
    }

    @Override
    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    @Override
    public String getUserEmail()
    {
        return userEmail;
    }

    @Override
    public void setUserEmail( String userEmail )
    {
        this.userEmail = userEmail;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @Override
    public String getPhotoUrl()
    {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl( String photoUrl )
    {
        this.photoUrl = photoUrl;
    }
}
