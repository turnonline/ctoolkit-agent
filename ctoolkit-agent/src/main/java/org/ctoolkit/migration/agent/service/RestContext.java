package org.ctoolkit.migration.agent.service;

/**
 * Rest context store meta info for rest calls, like email of authorized user.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface RestContext
{
    String getUserEmail();

    void setUserEmail(String userEmail);
}
