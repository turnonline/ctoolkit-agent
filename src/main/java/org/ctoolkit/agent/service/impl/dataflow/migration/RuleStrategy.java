package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;

/**
 * Interface for defining various strategies for {@link MigrationSetKindOpRule}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface RuleStrategy
{
    boolean apply( MigrationSetKindOpRule rule, Entity entity );

    boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity);

    String[] allowedTypes();
}
