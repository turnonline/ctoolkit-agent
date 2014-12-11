package org.ctoolkit.bulkloader.retired;

import com.comvai.auth.exception.AuthenticatedException;
import com.comvai.auth.model.AuthUser;
import com.comvai.auth.model.SessionUser;
import com.comvai.auth.server.AuthService;

/**
 * see #70
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class AuthServiceImpl
        implements AuthService
{
    public AuthServiceImpl()
    {
    }

    @Override
    public AuthUser authenticate( SessionUser sessionUser ) throws AuthenticatedException
    {
        return null;
    }

    @Override
    public AuthUser getAuthUserById( String authUserId )
    {
        return null;
    }

    @Override
    public boolean isAuthorized( AuthUser authUser )
    {
        return false;
    }

    @Override
    public String decodeFacebookAppSecret( String encodedAppSecret )
    {
        return null;
    }
}
