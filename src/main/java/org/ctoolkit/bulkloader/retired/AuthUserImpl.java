package org.ctoolkit.bulkloader.retired;

import com.comvai.auth.model.AuthUser;
import com.comvai.auth.model.LoginProvider;

/**
 * It's here just to suppress compilation error (see #70):
 * [ERROR] Errors in 'com/comvai/gwt/client/event/CachedEventBus.java'
 * [ERROR] Line 65: Failed to resolve 'com.comvai.gwt.common.command.CommandRemoteService' via deferred binding
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class AuthUserImpl
        implements AuthUser
{
    private static final long serialVersionUID = 1L;

    private String email;

    public AuthUserImpl()
    {
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    @Override
    public LoginProvider getLoginProvider()
    {
        return null;
    }

    @Override
    public String getUserId()
    {
        return null;
    }

    @Override
    public String getUserInfo()
    {
        return null;
    }

    @Override
    public String getLocale()
    {
        return null;
    }
}
