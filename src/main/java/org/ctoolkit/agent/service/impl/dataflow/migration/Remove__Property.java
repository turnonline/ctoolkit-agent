package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

import javax.inject.Inject;

/**
 * Use case - REMOVE - property
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Remove__Property
        extends UseCaseBase
{
    @Inject
    public Remove__Property( IRuleStrategyResolver ruleStrategyResolver )
    {
        super( ruleStrategyResolver );
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isRemove( operation ) &&
                operation.getKind() != null &&
                operation.getProperty() != null;
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
        return true;
    }

    @Override
    public boolean removeEntity()
    {
        return false;
    }
}
