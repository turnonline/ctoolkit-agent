package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

/**
 * Base rule strategy for mathematical operations
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class RuleStrategyMathOpBase
        extends RuleStrategyBase
{
    public RuleStrategyMathOpBase( EntityEncoder encoder )
    {
        super( encoder );
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
                return apply( entity.getTimestamp( property ).toSqlTimestamp().getTime(), Long.valueOf( rule.getValue() ) );
            }
        }

        return false;
    }

    @Override
    public AllowedType[] allowedTypes()
    {
        return new AllowedType[]{
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_LONG ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_DATE )
        };
    }

    protected abstract boolean apply( Double entityProperty, Double ruleProperty );

    protected abstract boolean apply( Long entityProperty, Long ruleProperty );
}
