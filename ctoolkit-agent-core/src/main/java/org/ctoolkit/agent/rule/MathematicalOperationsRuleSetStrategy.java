package org.ctoolkit.agent.rule;

import com.google.common.base.Charsets;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Rule set algebraic strategy for mathematical operations - '=','<','>','<=','>='
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MathematicalOperationsRuleSetStrategy
        implements RuleSetStrategy
{
    public static final RuleSetStrategy INSTANCE_EQ = new MathematicalOperationsRuleSetStrategy( Operation.EQ );
    public static final RuleSetStrategy INSTANCE_LT = new MathematicalOperationsRuleSetStrategy( Operation.LT );
    public static final RuleSetStrategy INSTANCE_LTE = new MathematicalOperationsRuleSetStrategy( Operation.LTE );
    public static final RuleSetStrategy INSTANCE_GT = new MathematicalOperationsRuleSetStrategy( Operation.GT );
    public static final RuleSetStrategy INSTANCE_GTE = new MathematicalOperationsRuleSetStrategy( Operation.GTE );

    private Operation operation;

    public MathematicalOperationsRuleSetStrategy( Operation operation )
    {
        this.operation = operation;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean apply( MigrationSetPropertyRule rule, EntityExportData entityExportData )
    {
        EntityExportData.Property property = entityExportData.getProperties().get( rule.getProperty() );
        if ( property != null )
        {
            Object convertedRuleValue = convertValue( rule.getValue(), property, rule );

            switch ( operation )
            {
                case EQ:
                {
                    return property.getValue().equals( convertedRuleValue );
                }
                case LT:
                {
                    return compareTo( property, convertedRuleValue ) < 0;
                }
                case LTE:
                {
                    return compareTo( property, convertedRuleValue ) <= 0;
                }
                case GT:
                {
                    return compareTo( property, convertedRuleValue ) > 0;
                }
                case GTE:
                {
                    return compareTo( property, convertedRuleValue ) >= 0;
                }
            }
        }

        // return true if property was not found - it means that we do not want to filter row if property is not found
        return true;
    }

    private Object convertValue( Object convertedValue,
                                 EntityExportData.Property property,
                                 MigrationSetPropertyRule rule )
    {
        if ( property.getValue() instanceof Integer )
        {
            convertedValue = Integer.valueOf( rule.getValue() );
        }
        else if ( property.getValue() instanceof Long )
        {
            convertedValue = Long.valueOf( rule.getValue() );
        }
        else if ( property.getValue() instanceof Float )
        {
            convertedValue = Float.valueOf( rule.getValue() );
        }
        else if ( property.getValue() instanceof Double )
        {
            convertedValue = Double.valueOf( rule.getValue() );
        }
        else if ( property.getValue() instanceof BigDecimal )
        {
            convertedValue = new BigDecimal( rule.getValue() );
        }
        else if ( property.getValue() instanceof Boolean )
        {
            convertedValue = Boolean.valueOf( rule.getValue() );
        }
        else if ( property.getValue() instanceof byte[] )
        {
            convertedValue = rule.getValue().getBytes( Charsets.UTF_8 );
        }
        else if ( property.getValue() instanceof Date )
        {
            convertedValue = new Date( Long.valueOf( rule.getValue() ) );
        }

        return convertedValue;
    }

    @SuppressWarnings( "unchecked" )
    private int compareTo( EntityExportData.Property property, Object convertedValue )
    {
        return ( ( Comparable ) property.getValue() ).compareTo( convertedValue );
    }
}
