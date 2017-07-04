package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import java.util.Arrays;

/**
 * Base rule strategy for mathematical operations
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class RuleStrategyMathOpBase
        implements RuleStrategy
{
    private final EntityEncoder encoder;

    public RuleStrategyMathOpBase( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public boolean apply( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        ChangeSetEntityProperty changeSetEntityProperty = encoder.encode( property, entity.getValue( property ) );

        switch ( changeSetEntityProperty.getType() )
        {
            case ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE:
            {
                return apply( entity.getDouble( property ), Double.valueOf( rule.getValue() ) );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_LONG:
            {
                return apply( entity.getLong( property ), Long.valueOf( rule.getValue() ) );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_DATE:
            {
                return apply( entity.getTimestamp( property ).getSeconds(), Long.valueOf( rule.getValue() ) );
            }
        }

        return false;
    }

    @Override
    public boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        ChangeSetEntityProperty changeSetEntityProperty = encoder.encode( property, entity.getValue( property ) );

        return Arrays.asList( allowedTypes() ).contains( changeSetEntityProperty.getType() );
    }

    @Override
    public String[] allowedTypes()
    {
        return new String[]{
                ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE,
                ChangeSetEntityProperty.PROPERTY_TYPE_LONG,
                ChangeSetEntityProperty.PROPERTY_TYPE_DATE,
        };
    }

    protected abstract boolean apply( Double entityProperty, Double ruleProperty );

    protected abstract boolean apply( Long entityProperty, Long ruleProperty );
}
