package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.MigrationSetKindOpRuleSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.resource.MigrationSetKindOperationAdd;
import org.ctoolkit.agent.resource.MigrationSetKindOperationChange;
import org.ctoolkit.agent.resource.MigrationSetKindOperationRemove;

/**
 * Base use case definition
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class UseCaseBase
        implements UseCase
{
    private final IRuleStrategyResolver ruleStrategyResolver;

    public UseCaseBase( IRuleStrategyResolver ruleStrategyResolver )
    {
        this.ruleStrategyResolver = ruleStrategyResolver;
    }

    public boolean applyRuleSet( MigrationSetKindOpRuleSet ruleSet, final Entity entity )
    {
        return ruleStrategyResolver.apply( ruleSet, entity );
    }

    /**
     * Determine if operation is type of ADD
     *
     * @param operation {@link MigrationSetKindOperation}
     * @return <code>true</code> if operation is type of ADD
     */
    protected boolean isAdd( MigrationSetKindOperation operation )
    {
        return operation instanceof MigrationSetKindOperationAdd;
    }

    /**
     * Determine if operation is type of REMOVE
     *
     * @param operation {@link MigrationSetKindOperation}
     * @return <code>true</code> if operation is type of REMOVE
     */
    protected boolean isRemove( MigrationSetKindOperation operation )
    {
        return operation instanceof MigrationSetKindOperationRemove;
    }

    /**
     * Determine if operation is type of CHANGE
     *
     * @param operation {@link MigrationSetKindOperation}
     * @return <code>true</code> if operation is type of CHANGE
     */
    protected boolean isChange( MigrationSetKindOperation operation )
    {
        return operation instanceof MigrationSetKindOperationChange;
    }
}
