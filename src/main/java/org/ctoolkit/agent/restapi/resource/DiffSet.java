package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;
import java.util.List;

/**
 * Root diff entity.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DiffSet
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private List<EntityDiff> entities;

    public DiffSet()
    {
    }

    public DiffSet( Long id )
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public List<EntityDiff> getEntities()
    {
        return entities;
    }

    public void setEntities( List<EntityDiff> entities )
    {
        this.entities = entities;
    }
}
