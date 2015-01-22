package org.ctoolkit.agent.restapi.resource;

import java.util.List;

/**
 * The entity schema that represents a concrete set of entities.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntitySchema
{
    /**
     * The required entity kind.
     */
    private String kind;

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
}
