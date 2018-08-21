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
public class MathOpsEqualsRuleStrategyTest
{
    private RuleStrategy strategy = MathOpsRuleStrategy.INSTANCE_EQ;

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
    public void apply_StringNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, exportData( "name", "Jack" ) ) );
    }

    @Test
    public void apply_StringEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, exportData( "name", "John" ) ) );
    }

    // -- Integer

    @Test
    public void apply_IntegerNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 31 ) ) );
    }

    @Test
    public void apply_IntegerEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 30 ) ) );
    }

    // -- Long

    @Test
    public void apply_LongNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 31L ) ) );
    }

    @Test
    public void apply_LongEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 30L ) ) );
    }

    // -- Float

    @Test
    public void apply_FloatNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 31F ) ) );
    }

    @Test
    public void apply_FloatEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 30F ) ) );
    }

    // -- Double

    @Test
    public void apply_DoubleNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", 31D ) ) );
    }

    @Test
    public void apply_DoubleEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", 30D ) ) );
    }

    // -- BigDecimal

    @Test
    public void apply_BigDecimalNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, exportData( "age", BigDecimal.valueOf( 31 ) ) ) );
    }

    @Test
    public void apply_BigDecimalEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, exportData( "age", BigDecimal.valueOf( 30 ) ) ) );
    }

    // -- Boolean

    @Test
    public void apply_BooleanNotEquals()
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

        assertTrue( strategy.apply( rule, exportData( "active", true ) ) );
    }

    // -- Byte array

    @Test
    public void apply_ByteArrayNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "data" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, exportData( "data", new byte[]{'j'} ) ) );
    }

    @Test
    public void apply_ByteArrayEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "active" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, exportData( "data", new byte[]{'J', 'o', 'h', 'n'} ) ) );
    }

    // -- Date

    @Test
    public void apply_DateNotEquals()
    {
        MigrationSetPropertyRule rule = new MigrationSetPropertyRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1" );

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

        assertTrue( strategy.apply( rule, exportData( "createDate", calendar.getTime() ) ) );
    }
}