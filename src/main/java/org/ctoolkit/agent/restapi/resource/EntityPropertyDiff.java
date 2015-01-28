package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;

/**
 * The entity property diff definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityPropertyDiff
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private DiffRule rule;

    private String property;

    public EntityPropertyDiff()
    {
    }

    public DiffRule getRule()
    {
        return rule;
    }

    public void setRule( DiffRule rule )
    {
        this.rule = rule;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty( String property )
    {
        this.property = property;
    }
}
