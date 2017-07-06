package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;

import javax.inject.Inject;

/**
 * Use case - CHANGE - nwe name + new type + new value
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Change__NewName_NewType_NewValue
        extends UseCaseBase
{
    private final EntityDecoder decoder;

    @Inject
    public Change__NewName_NewType_NewValue( IRuleStrategyResolver ruleStrategyResolver,
                                             EntityDecoder decoder )
    {
        super( ruleStrategyResolver );
        this.decoder = decoder;
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isChange( operation ) &&
                operation.getKind() != null &&
                operation.getProperty() != null &&
                operation.getNewKind() == null &&
                operation.getNewName() != null &&
                operation.getNewType() != null &&
                operation.getNewValue() != null;
    }

    @Override
    public String name( MigrationSetKindOperation operation )
    {
        return operation.getNewName();
    }

    @Override
    public Value<?> value( MigrationSetKindOperation operation, Entity entity )
    {
        return decoder.decode( operation.getNewType(), operation.getNewValue() );
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
