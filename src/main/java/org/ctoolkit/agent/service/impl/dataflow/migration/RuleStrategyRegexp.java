package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Rule strategy for regexp operation
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyRegexp
        implements RuleStrategy
{
    private final EntityEncoder encoder;

    @Inject
    public RuleStrategyRegexp( EntityEncoder encoder )
    {
        this.encoder = encoder;
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
                ChangeSetEntityProperty.PROPERTY_TYPE_STRING
        };
    }
}
