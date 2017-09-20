package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * Rule strategy for regexp operation
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyRegexp
        extends RuleStrategyBase
{
    @Inject
    public RuleStrategyRegexp( EntityEncoder encoder )
    {
        super( encoder );
    }

    @Override
    public boolean apply( MigrationSetKindOpRule rule, Entity entity )
    {
        String property = rule.getProperty();
        ChangeSetEntityProperty changeSetEntityProperty = encoder.encode( property, entity.getValue( property ) );

        Pattern pattern = Pattern.compile( rule.getValue() );
        return pattern.matcher( changeSetEntityProperty.getValue() ).matches();
    }

    @Override
    public AllowedType[] allowedTypes()
    {
        return new AllowedType[]{
                new AllowedType( ChangeSetEntityProperty.PROPERTY_TYPE_STRING )
        };
    }
}
