package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * Use case - CHANGE - new value
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Change__NewValue
        extends UseCaseBase
{
    private final EntityEncoder encoder;

    private final EntityDecoder decoder;

    @Inject
    public Change__NewValue( IRuleStrategyResolver ruleStrategyResolver,
                             EntityEncoder encoder,
                             EntityDecoder decoder )
    {
        super( ruleStrategyResolver );
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public boolean apply( MigrationSetKindOperation operation )
    {
        return isChange( operation ) &&
                operation.getKind() != null &&
                operation.getProperty() != null &&
                operation.getNewKind() == null &&
                operation.getNewName() == null &&
                operation.getNewType() == null &&
                operation.getNewValue() != null;
    }

    @Override
    public String name( MigrationSetKindOperation operation )
    {
        return operation.getProperty();
    }

    @Override
    public Value<?> value( MigrationSetKindOperation operation, Entity entity )
    {
        String property = operation.getProperty();

        ChangeSetEntityProperty changeSetEntityProperty = encoder.encode( property, entity.getValue( property ) );
        return decoder.decode( changeSetEntityProperty.getType(), operation.getNewValue() );
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
