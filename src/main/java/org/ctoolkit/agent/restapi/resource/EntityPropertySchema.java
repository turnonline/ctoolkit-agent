package org.ctoolkit.agent.restapi.resource;

/**
 * The entity's schema property definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityPropertySchema
{
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
}
