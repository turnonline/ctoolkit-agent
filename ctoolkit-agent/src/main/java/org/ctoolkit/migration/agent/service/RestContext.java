package org.ctoolkit.migration.agent.service;

/**
 * Rest context store meta info for rest calls, like email of authorized user.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface RestContext
{
    String getUserId();

    void setUserId(String userId);

    String getUserEmail();

    void setUserEmail(String userEmail);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);
}
