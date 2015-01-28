package org.ctoolkit.agent.restapi.resource;

import java.io.Serializable;
import java.util.List;

/**
 * The entity schema that represents a concrete set of entities.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntitySchema
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * The required entity kind.
     */
    private String entityKind;

    /**
     * The required entity identifier type 'id' or 'name'.
     */
    private String keyType;

    /**
     * The optional parent entity kind.
     */
    private String parentKind;

    /**
     * The optional parent entity identifier type 'id' or 'name'.
     */
    private String parentKeyType;

    /**
     * Boolean indication whether entire entity is indexed or not.
     */
    private boolean index;

    /**
     * Optional entity properties
     */
    private List<EntityPropertySchema> properties;

    public EntitySchema()
    {
    }

    public String getEntityKind()
    {
        return entityKind;
    }

    public void setEntityKind( String entityKind )
    {
        this.entityKind = entityKind;
    }

    public String getKeyType()
    {
        return keyType;
    }

    public void setKeyType( String keyType )
    {
        this.keyType = keyType;
    }

    public String getParentKind()
    {
        return parentKind;
    }

    public void setParentKind( String parentKind )
    {
        this.parentKind = parentKind;
    }

    public String getParentKeyType()
    {
        return parentKeyType;
    }

    public void setParentKeyType( String parentKeyType )
    {
        this.parentKeyType = parentKeyType;
    }

    public boolean isIndex()
    {
        return index;
    }

    public void setIndex( boolean index )
    {
        this.index = index;
    }

    public List<EntityPropertySchema> getProperties()
    {
        return properties;
    }

    public void setProperties( List<EntityPropertySchema> properties )
    {
        this.properties = properties;
    }
}
