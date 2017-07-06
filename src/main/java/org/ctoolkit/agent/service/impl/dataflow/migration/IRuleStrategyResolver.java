package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.MigrationSetKindOpRuleSet;

/**
 * Interface for rule strategy resolver
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface IRuleStrategyResolver
{
    /**
     * Apply rule set to entity
     *
     * @param ruleSet {@link MigrationSetKindOpRuleSet }
     * @param entity  {@link Entity}
     * @return <code>true</code> if rule set is applicable to entity, <code>false</code> otherwise
     */
    boolean apply( MigrationSetKindOpRuleSet ruleSet, final Entity entity );
}
