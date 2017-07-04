package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

/**
 * Use case - CHANGE - new name
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Change__NewName
        extends UseCaseBase
{
    public Change__NewName( RuleStrategyResolver ruleStrategyResolver )
    {
        super( ruleStrategyResolver );
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isChange( operation ) &&
                operation.getKind() != null &&
                operation.getProperty() != null &&
                operation.getNewKind() == null &&
                operation.getNewName() != null &&
                operation.getNewType() == null &&
                operation.getNewValue() == null;
    }

    @Override
    public String name( MigrationSetKindOperation operation )
    {
        return operation.getNewName();
    }

    @Override
    public Value<?> value( MigrationSetKindOperation operation, Entity entity )
    {
        return entity.getValue( operation.getProperty() );
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
