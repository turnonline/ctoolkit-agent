package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;

/**
 * Interface for defining various strategies for {@link MigrationSetKindOpRule}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface RuleStrategy
{
    /**
     * Returns true if entity meets rule conditions
     *
     * @param rule   {@link MigrationSetKindOpRule}
     * @param entity {@link Entity}
     * @return <code>true</code> if entity meets rule conditions, <code>false</code> otherwise
     */
    boolean apply( MigrationSetKindOpRule rule, Entity entity );

    /**
     * Returns true if type is allowed, i.e. {@link RuleStrategyRegexp} can evaluate on <code>string</code> types only.
     *
     * @param rule   {@link MigrationSetKindOpRule}
     * @param entity {@link Entity}
     * @return <code>true</code> if type is allowed, <code>false</code> otherwise
     */
    boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity );

    /**
     * Returns encoded property. Method {@link RuleStrategy#isTypeAllowed(MigrationSetKindOpRule, Entity)} must be called
     * before retrieving of ChangeSetEntityProperty
     *
     * @return ChangeSetEntityProperty
     */
    ChangeSetEntityProperty encodedProperty();

    /**
     * Return array of allowed types for strategy
     *
     * @return array ow allowed types
     */
    String[] allowedTypes();
}
