package org.ctoolkit.agent.service.impl;

import org.ctoolkit.agent.service.RestContext;

/**
 * Request scoped rest context implementation. Store info about rest
 * metadata in thread local variable.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class RestContextThreadLocal
        implements RestContext
{
    private String userId;

    private String userEmail;

    private String displayName;

    private String photoUrl;

    private String gtoken;

    private String onBehalfOfAgentUrl;

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

    public String getGtoken()
    {
        return gtoken;
    }

    public void setGtoken( String gtoken )
    {
        this.gtoken = gtoken;
    }

    public String getOnBehalfOfAgentUrl()
    {
        return onBehalfOfAgentUrl;
    }

    public void setOnBehalfOfAgentUrl( String onBehalfOfAgentUrl )
    {
        this.onBehalfOfAgentUrl = onBehalfOfAgentUrl;
    }

    @Override
    public String toString()
    {
        return "RestContextThreadLocal{" +
                "userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", gtoken='" + gtoken + '\'' +
                ", onBehalfOfAgentUrl='" + onBehalfOfAgentUrl + '\'' +
                '}';
    }
}
