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
    private String userEmail;

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
}
