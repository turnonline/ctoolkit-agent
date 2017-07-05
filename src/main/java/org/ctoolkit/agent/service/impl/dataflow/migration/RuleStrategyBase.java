package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import java.util.Arrays;

/**
 * Base for rule strategies
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class RuleStrategyBase
        implements RuleStrategy
{
    protected final EntityEncoder encoder;

    private ChangeSetEntityProperty encodedProperty;

    public RuleStrategyBase( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        encodedProperty = encoder.encode( property, entity.getValue( property ) );

        return Arrays.asList( allowedTypes() ).contains( encodedProperty.getType() );
    }

    @Override
    public ChangeSetEntityProperty encodedProperty()
    {
        return encodedProperty;
    }
}
