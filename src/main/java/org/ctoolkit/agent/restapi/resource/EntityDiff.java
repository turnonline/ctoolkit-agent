package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;
import java.util.List;

/**
 * The entity diff definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityDiff
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String kind;

    private List<EntityPropertyDiff> properties;

    public EntityDiff()
    {
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public List<EntityPropertyDiff> getProperties()
    {
        return properties;
    }

    public void setProperties( List<EntityPropertyDiff> properties )
    {
        this.properties = properties;
    }
}
