package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOpRuleSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

/**
 * Interface for defining of migration use cases
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface UseCase
{
    /**
     * Used by {@link UseCaseResolver} to determine if this use case is applied for current {@link MigrationSetKindOperation}
     *
     * @param operation {@link MigrationSetKindOperation}
     * @return <code>true</code> if this use case should be applied for current {@link MigrationSetKindOperation}, <code>false</code> otherwise
     */
    boolean apply( MigrationSetKindOperation operation );

    /**
     * Used to determine if migration operation is applicable for {@link Entity}
     *
     * @param ruleSet {@link MigrationSetKindOpRuleSet}
     * @param entity  {@link Entity}
     * @return <code>true</code> if migration operation should be applied for current {@link Entity}, <code>false</code> otherwise
     */
    boolean applyRuleSet( MigrationSetKindOpRuleSet ruleSet, Entity entity );

    /**
     * Return entity property name
     *
     * @param operation {@link MigrationSetKindOperation}
     * @return entity property name
     */
    String name( MigrationSetKindOperation operation );

    /**
     * Return entity property {@link Value}
     *
     * @param operation {@link MigrationSetKindOperation}
     * @param entity    {@link Entity}
     * @return entity property value
     */
    Value<?> value( MigrationSetKindOperation operation, Entity entity );

    /**
     * Marker to determine if old property should be removed. For instance if type of property is changed,
     * the new property is created and old one must be removed. Hovever if change of value is applied there is
     * no need to remove old property, just update existing one.
     *
     * @return <code>true</code> if old property should be removed, <code>false</code> otherwise
     */
    boolean removeOldProperty();

    /**
     * Marker to determine if the whole entity should be removed. For some use cases like {@link Remove__Kind}
     * the whole entity needs to be removed
     *
     * @return <code>true</code> if entity should be removed, <code>false</code> otherwise
     */
    boolean removeEntity();
}
