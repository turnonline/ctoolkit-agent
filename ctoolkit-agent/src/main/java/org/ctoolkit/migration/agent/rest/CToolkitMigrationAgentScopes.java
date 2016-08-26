package org.ctoolkit.migration.agent.rest;

/**
 * Available OAuth 2.0 scopes for use with the CToolkit migration API.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class CToolkitMigrationAgentScopes
{
    public static final String EMAIL = "https://www.googleapis.com/auth/userinfo.email";

    private CToolkitMigrationAgentScopes()
    {
    }

    public static java.util.Set<String> all()
    {
        java.util.Set<String> set = new java.util.HashSet<String>();
        set.add( EMAIL );
        return java.util.Collections.unmodifiableSet( set );
    }
}
