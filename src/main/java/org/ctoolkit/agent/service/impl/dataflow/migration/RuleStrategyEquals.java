package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * Rule strategy for equals operation
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyEquals
        extends RuleStrategyBase
{
    @Inject
    public RuleStrategyEquals( EntityEncoder encoder )
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
            case ChangeSetEntityProperty.PROPERTY_TYPE_STRING:
            case ChangeSetEntityProperty.PROPERTY_TYPE_REFERENCE:
            {
                return rule.getValue().equals( changeSetEntityProperty.getValue() );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE:
            {
                return Double.valueOf( rule.getValue() ).equals( Double.valueOf( changeSetEntityProperty.getValue() ) );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_LONG:
            {
                return Long.valueOf( rule.getValue() ).equals( Long.valueOf( changeSetEntityProperty.getValue() ) );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN:
            {
                return Boolean.valueOf( rule.getValue() ).equals( Boolean.valueOf( changeSetEntityProperty.getValue() ) );
            }
            case ChangeSetEntityProperty.PROPERTY_TYPE_NULL:
            {
                return rule.getValue().equals( "null" );
            }
        }

        return false;
    }

    @Override
    public AllowedType[] allowedTypes()
    {
        return new AllowedType[]{
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_STRING ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_LONG ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_NULL ),
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_REFERENCE )
        };
    }
}
