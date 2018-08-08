package org.ctoolkit.agent.rule;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule set strategy for regular expression
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RegexRuleStrategy
        implements RuleStrategy
{
    public static final RuleStrategy INSTANCE = new RegexRuleStrategy();

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean apply( MigrationSetPropertyRule rule, EntityExportData entityExportData )
    {
        EntityExportData.Property property = entityExportData.getProperties().get( rule.getProperty() );
        String ruleValue = rule.getValue();

        if ( property != null && property.getValue() instanceof String )
        {
            Pattern pattern = Pattern.compile( ruleValue );
            Matcher matcher = pattern.matcher( ( String ) property.getValue() );
            return matcher.matches();
        }

        // return true if property was not found - it means that we do not want to filter row if property is not found
        return true;
    }
}
