package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.ListValue;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import org.ctoolkit.agent.exception.RuleStrategyException;
import org.ctoolkit.agent.resource.ChangeSetEntityProperty;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.resource.MigrationSetKindOpRuleSet;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Rule strategy resolver
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyResolver
        implements IRuleStrategyResolver
{
    private static final String MESSAGE_TYPE_NOT_ALLOWED = "Type is not allowed for rule: {0}. Allowed types are: {1} but actual entity property ''{2}'' is type of {3}.";
    private static final String MESSAGE_STRATEGY_NOT_EXISTS = "No rule strategy exists for rule operation: {0}. Allowed operations are: {1} but actual operation is {2}";

    private Map<String, RuleStrategy> strategies = new HashMap<>();

    private static final String[] ALLOWED_OPERATIONS = new String[]{
            MigrationSetKindOpRule.EQUALS,
            MigrationSetKindOpRule.LOWER_THAN,
            MigrationSetKindOpRule.LOWER_THAN_EQUALS,
            MigrationSetKindOpRule.GREATER_THAN,
            MigrationSetKindOpRule.GREATER_THAN_EQUALS,
            MigrationSetKindOpRule.REGEXP
    };

    @Inject
    public RuleStrategyResolver( RuleStrategyEquals ruleStrategyEquals,
                                 RuleStrategyLowerThan ruleStrategyLowerThan,
                                 RuleStrategyLowerThanEquals ruleStrategyLowerThanEquals,
                                 RuleStrategyGreaterThan ruleStrategyGreaterThan,
                                 RuleStrategyGreaterThanEquals ruleStrategyGreaterThanEquals,
                                 RuleStrategyRegexp ruleStrategyRegexp )
    {
        strategies.put( MigrationSetKindOpRule.EQUALS, ruleStrategyEquals );
        strategies.put( MigrationSetKindOpRule.LOWER_THAN, ruleStrategyLowerThan );
        strategies.put( MigrationSetKindOpRule.LOWER_THAN_EQUALS, ruleStrategyLowerThanEquals );
        strategies.put( MigrationSetKindOpRule.GREATER_THAN, ruleStrategyGreaterThan );
        strategies.put( MigrationSetKindOpRule.GREATER_THAN_EQUALS, ruleStrategyGreaterThanEquals );
        strategies.put( MigrationSetKindOpRule.REGEXP, ruleStrategyRegexp );
    }

    @Override
    public boolean apply( MigrationSetKindOpRuleSet ruleSet, final Entity entity )
    {
        boolean apply;

        if ( ruleSet != null )
        {
            // set default operation as AND if missing
            if ( ruleSet.getOperation() == null )
            {
                ruleSet.setOperation( MigrationSetKindOpRuleSet.AND );
            }

            switch ( ruleSet.getOperation() )
            {
                case MigrationSetKindOpRuleSet.AND:
                {
                    apply = Iterators.all( ruleSet.getRules().iterator(), new StrategyPredicate( entity ) );
                    break;
                }
                case MigrationSetKindOpRuleSet.OR:
                {
                    apply = Iterators.any( ruleSet.getRules().iterator(), new StrategyPredicate( entity ) );
                    break;
                }
                default:
                {
                    throw new IllegalArgumentException( "Unknown operation for rule set: '" + ruleSet + "'" );
                }
            }
        }
        else
        {
            // if no rule set is defined, proceed entity to further processing
            apply = true;
        }

        return apply;
    }

    /**
     * Strategy predicate is used to retrieve correct rule strategy
     * and determine if specified rule can apply to entity
     */
    private class StrategyPredicate
            implements Predicate<MigrationSetKindOpRule>
    {
        private final Entity entity;

        private StrategyPredicate( Entity entity )
        {
            this.entity = entity;
        }

        @Override
        public boolean apply( @Nullable MigrationSetKindOpRule rule )
        {
            checkNotNull( rule, "MigrationSetKindOpRule cannot be null!" );

            RuleStrategy ruleStrategy = strategies.get( rule.getOperation() );
            if ( ruleStrategy != null )
            {
                // check if type is allowed - for instance lower than strategy can be allowed only for long, double, etc...
                if ( ruleStrategy.isTypeAllowed( rule, entity ) )
                {
                    // check if entity applies to rule
                    return ruleStrategy.apply( rule, entity );
                }

                // throw exception if type is not allowed
                ChangeSetEntityProperty changeSetEntityProperty = ruleStrategy.encodedProperty( rule, entity );
                String listSuffix = entity.getValue( rule.getProperty() ) instanceof ListValue ? " (list)" : ""; // add list suffix for list values

                throw new RuleStrategyException( MessageFormat.format( MESSAGE_TYPE_NOT_ALLOWED,
                        rule,
                        Arrays.toString( ruleStrategy.allowedTypes() ),
                        changeSetEntityProperty.getName(),
                        changeSetEntityProperty.getType() + listSuffix )
                );
            }

            // throw exception if rule strategy cannot be found - this can happen for xml/json typos
            throw new RuleStrategyException( MessageFormat.format( MESSAGE_STRATEGY_NOT_EXISTS,
                    rule,
                    Arrays.toString( ALLOWED_OPERATIONS ),
                    rule.getOperation() )
            );
        }
    }
}
