package org.ctoolkit.agent.rule;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import static org.ctoolkit.agent.Mocks.exportData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link MathOpsRuleStrategy}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MathOpsGreaterThanRuleStrategyTest
{
    private RuleStrategy strategy = MathOpsRuleStrategy.INSTANCE_GT;

    @Test
    public void apply_PropertyNotFound()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, new EntityExportData() ) );
    }

    // -- String

    @Test
    public void apply_StringLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, exportData( "name", "Joh" ) ) );
    }

    @Test
    public void apply_StringEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, exportData( "name", "John" ) ) );
    }

    @Test
    public void apply_StringGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, exportData( "name", "Johny" ) ) );
    }

    // -- Integer

    @Test
    public void apply_IntegerLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 29 ) ) );
    }

    @Test
    public void apply_IntegerEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 30 ) ) );
    }

    @Test
    public void apply_IntegerGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 31 ) ) );
    }

    // -- Long

    @Test
    public void apply_LongLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 29L ) ) );
    }

    @Test
    public void apply_LongEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 30L ) ) );
    }

    @Test
    public void apply_LongGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 31L ) ) );
    }

    // -- Float

    @Test
    public void apply_FloatLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 29F ) ) );
    }

    @Test
    public void apply_FloatEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 30F ) ) );
    }

    @Test
    public void apply_FloatGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 31F ) ) );
    }

    // -- Double

    @Test
    public void apply_DoubleLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 29D ) ) );
    }

    @Test
    public void apply_DoubleEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 30D ) ) );
    }

    @Test
    public void apply_DoubleGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 31D ) ) );
    }

    // -- BigDecimal

    @Test
    public void apply_BigDecimalLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", BigDecimal.valueOf( 29 ) ) ) );
    }

    @Test
    public void apply_BigDecimalEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", BigDecimal.valueOf( 30 ) ) ) );
    }

    @Test
    public void apply_BigDecimalGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", BigDecimal.valueOf( 31 ) ) ) );
    }

    // -- Boolean

    @Test
    public void apply_BooleanLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "active" );
        rule.setValue( "true" );

        assertFalse( strategy.apply( rule, exportData( "active", false ) ) );
    }

    @Test
    public void apply_BooleanEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "active" );
        rule.setValue( "true" );

        assertFalse( strategy.apply( rule, exportData( "active", true ) ) );
    }

    // -- Date

    @Test
    public void apply_DateLower()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764900000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertFalse( strategy.apply( rule, exportData( "createDate", calendar.getTime() ) ) );
    }

    @Test
    public void apply_DateEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764800000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertFalse( strategy.apply( rule, exportData( "createDate", calendar.getTime() ) ) );
    }

    @Test
    public void apply_DateGreater()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764600000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertTrue( strategy.apply( rule, exportData( "createDate", calendar.getTime() ) ) );
    }
}