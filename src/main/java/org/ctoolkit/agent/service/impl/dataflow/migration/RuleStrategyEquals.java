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
        implements RuleStrategy
{
    private final EntityEncoder encoder;

    @Inject
    public RuleStrategyEquals( EntityEncoder encoder )
    {
        this.encoder = encoder;
    }

    @Override
    public boolean apply( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        ChangeSetEntityProperty changeSetEntityProperty = encoder.encode( property, entity.getValue( property ) );
        return rule.getValue().equals( changeSetEntityProperty.getValue() );
    }

    @Override
    public boolean isTypeAllowed( MigrationSetKindOpRule rule, Entity entity )
    {
        return true;
    }

    @Override
    public String[] allowedTypes()
    {
        return new String[]{
                ChangeSetEntityProperty.PROPERTY_TYPE_STRING,
                ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE,
                ChangeSetEntityProperty.PROPERTY_TYPE_LONG,
                ChangeSetEntityProperty.PROPERTY_TYPE_DATE,
                ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN,
                ChangeSetEntityProperty.PROPERTY_TYPE_BLOB,
                ChangeSetEntityProperty.PROPERTY_TYPE_NULL,
                ChangeSetEntityProperty.PROPERTY_TYPE_KEY,
                ChangeSetEntityProperty.PROPERTY_TYPE_LIST_KEY,
                ChangeSetEntityProperty.PROPERTY_TYPE_LIST_LONG,
                ChangeSetEntityProperty.PROPERTY_TYPE_LIST_STRING,
        };
    }
}
