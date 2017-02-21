package org.ctoolkit.migration.agent.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class CtoolkitAgentConfiguration
        implements Serializable
{
    private static final long serialVersionUID = -7818971402505794544L;

    private String rootUrl;

    private String gtoken;

    public CtoolkitAgentConfiguration()
    {
    }

    public CtoolkitAgentConfiguration( String rootUrl, String gtoken )
    {
        this.rootUrl = rootUrl;
        this.gtoken = gtoken;
    }

    public String getRootUrl()
    {
        return rootUrl;
    }

    public String getGtoken()
    {
        return gtoken;
    }
}
