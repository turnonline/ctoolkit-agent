package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;

/**
 * The entity's schema property definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityPropertySchema
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Property name.
     */
    private String name;

    /**
     * Property type.
     */
    private String type;

    /**
     * Boolean indication whether this particular property is indexed or not.
     */
    private boolean index;

    public EntityPropertySchema()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public boolean isIndex()
    {
        return index;
    }

    public void setIndex( boolean index )
    {
        this.index = index;
    }
}
