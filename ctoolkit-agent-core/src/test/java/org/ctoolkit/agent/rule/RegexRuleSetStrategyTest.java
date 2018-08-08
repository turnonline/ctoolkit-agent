package org.ctoolkit.agent.rule;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;
import org.junit.Test;

import static org.ctoolkit.agent.Mocks.exportData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link RuleSetStrategy}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RegexRuleSetStrategyTest
{
    private RuleSetStrategy strategy = RegexRuleSetStrategy.INSTANCE;

    @Test
    public void apply_NotString()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "^\\d(2)$" );

        assertTrue( strategy.apply( rule, new EntityExportData() ) );
    }

    @Test
    public void apply_String_NoMatch()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "\\d{2}" );

        assertFalse( strategy.apply( rule, exportData( "name", "John" ) ) );
    }

    @Test
    public void apply_String_Match()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "\\d{2}" );

        assertTrue( strategy.apply( rule, exportData( "name", "34" ) ) );
    }
}