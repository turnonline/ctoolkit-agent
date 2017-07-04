package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

/**
 * Use case - ADD - new property + new type + new value (value is null)
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Add__NewName_NewType_NewValueNull
        extends UseCaseBase
{
    public Add__NewName_NewType_NewValueNull( RuleStrategyResolver ruleStrategyResolver )
    {
        super( ruleStrategyResolver );
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isAdd( operation ) &&
                operation.getKind() != null &&
                operation.getNewKind() == null &&
                operation.getNewName() != null &&
                operation.getNewType() != null &&
                operation.getNewValue() == null;
    }

    @Override
    public String name( MigrationSetKindOperation operation )
    {
        return operation.getProperty();
    }

    @Override
    public Value<?> value( MigrationSetKindOperation operation, Entity entity )
    {
        return new NullValue();
    }

    @Override
    public boolean removeOldProperty()
    {
        return false;
    }

    @Override
    public boolean removeEntity()
    {
        return false;
    }
}
