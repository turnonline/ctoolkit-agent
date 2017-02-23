package org.ctoolkit.agent.service;

/**
 * Rest context store meta info for rest calls, like email of authorized user.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface RestContext
{
    String getUserId();

    void setUserId( String userId );

    String getUserEmail();

    void setUserEmail( String userEmail );

    String getDisplayName();

    void setDisplayName( String displayName );

    String getPhotoUrl();

    void setPhotoUrl( String photoUrl );

    String getGtoken();

    void setGtoken( String gtoken );

    String getOnBehalfOfAgentUrl();

    void setOnBehalfOfAgentUrl( String onBehalfOfAgentUrl );
}