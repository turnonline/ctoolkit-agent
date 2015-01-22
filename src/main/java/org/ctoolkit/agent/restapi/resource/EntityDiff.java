package org.ctoolkit.agent.restapi.resource;

import java.util.List;

/**
 * The entity diff definition.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class EntityDiff
{
    private String kind;

    private List<EntityPropertyDiff> properties;
}
