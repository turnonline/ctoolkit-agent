package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;

/**
 * The diff rule definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DiffRule
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String rule;

    public DiffRule()
    {
    }

    public String getRule()
    {
        return rule;
    }

    public void setRule( String rule )
    {
        this.rule = rule;
    }
}
