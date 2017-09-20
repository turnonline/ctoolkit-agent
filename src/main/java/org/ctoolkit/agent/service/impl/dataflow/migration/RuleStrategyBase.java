package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.ListValue;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

/**
 * Base for rule strategies
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class RuleStrategyBase
        implements RuleStrategy
{
    protected final EntityEncoder encoder;

    public RuleStrategyBase( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity )
    {
        ChangeSetEntityProperty changeSetEntityProperty = encodedProperty( rule, entity );

        for ( AllowedType allowedType : allowedTypes() )
        {
            if ( allowedType.getType().equals( changeSetEntityProperty.getType() ) )
            {
                // if allowed type does not support list values and entity value is list, return false
                if ( !allowedType.isSupportList() && entity.getValue( rule.getProperty() ) instanceof ListValue )
                {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public ChangeSetEntityProperty encodedProperty( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        return encoder.encode( property, entity.getValue( property ) );
    }
}
