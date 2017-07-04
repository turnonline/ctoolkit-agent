package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

/**
 * Use case - REMOVE - kind
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Remove__Kind
        extends UseCaseBase
{
    public Remove__Kind( RuleStrategyResolver ruleStrategyResolver )
    {
        super( ruleStrategyResolver );
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isRemove( operation ) &&
                operation.getKind() != null &&
                operation.getProperty() == null;
    }

    @Override
    public String name( MigrationSetKindOperation operation )
    {
        return null;
    }

    @Override
    public Value<?> value( MigrationSetKindOperation operation, Entity entity )
    {
        return null;
    }

    @Override
    public boolean removeOldProperty()
    {
        return false;
    }

    @Override
    public boolean removeEntity()
    {
        return true;
    }
}
