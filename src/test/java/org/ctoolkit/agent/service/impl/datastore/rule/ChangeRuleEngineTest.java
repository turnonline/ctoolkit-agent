package org.ctoolkit.agent.service.impl.datastore.rule;

import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link ChangeRuleEngine}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeRuleEngineTest
{
    private EntityEncoder encoder = new EntityEncoder();

    private NewNameChangeRule newNameChangeRule = new NewNameChangeRule();
    private NewTypeChangeRule newTypeChangeRule = new NewTypeChangeRule( encoder );
    private NewValueChangeRule newValueChangeRule = new NewValueChangeRule( encoder );
    private NewNameNewTypeChangeRule newNameNewTypeChangeRule = new NewNameNewTypeChangeRule( encoder );
    private NewNameNewValueChangeRule newNameNewValueChangeRule = new NewNameNewValueChangeRule( encoder );
    private NewTypeNewValueChangeRule newTypeNewValueChangeRule = new NewTypeNewValueChangeRule( encoder );
    private NewNameNewTypeNewValueChangeRule newNameNewTypeNewValueChangeRule = new NewNameNewTypeNewValueChangeRule( encoder );

    private ChangeRuleEngine ruleEngine = new ChangeRuleEngine(
            newNameChangeRule,
            newTypeChangeRule,
            newValueChangeRule,
            newNameNewTypeChangeRule,
            newNameNewValueChangeRule,
            newTypeNewValueChangeRule,
            newNameNewTypeNewValueChangeRule
    );

    @Test
    public void testNewNameChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( "_name", null, null );

        assertTrue( rule instanceof NewNameChangeRule );
        assertEquals( "_name", rule.getName( "name", "_name" ) );
        assertEquals( 10, rule.getValue( 10, null, null ) );
    }

    @Test
    public void testNewTypeChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( null, "string", null );

        assertTrue( rule instanceof NewTypeChangeRule );
        assertEquals( "name", rule.getName( "name", null ) );
        assertEquals( 10, rule.getValue( "10", "int", null ) );
    }

    @Test
    public void testNewValueChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( null, null, "10" );

        assertTrue( rule instanceof NewValueChangeRule );
        assertEquals( "name", rule.getName( "name", null ) );
        assertEquals( "10", rule.getValue( "1", null, "10" ) );
    }

    @Test
    public void testNewNameNewTypeChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( "_name", "string", null );

        assertTrue( rule instanceof NewNameNewTypeChangeRule );
        assertEquals( "_name", rule.getName( "name", "_name" ) );
        assertEquals( 10, rule.getValue( "10", "int", null ) );
    }

    @Test
    public void testNewNameNewValueChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( "_name", null, "10" );

        assertTrue( rule instanceof NewNameNewValueChangeRule );
        assertEquals( "_name", rule.getName( "name", "_name" ) );
        assertEquals( "10", rule.getValue( "1", null, "10" ) );
    }

    @Test
    public void testNewTypeNewValueChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( null, "string", "10" );

        assertTrue( rule instanceof NewTypeNewValueChangeRule );
        assertEquals( "name", rule.getName( "name", "_name" ) );
        assertEquals( 10, rule.getValue( "1", "int", "10" ) );
    }

    @Test
    public void testNewNameNewTypeNewValueChangeRule() throws Exception
    {
        IChangeRule rule = ruleEngine.provideRule( "_name", "string", "10" );

        assertTrue( rule instanceof NewNameNewTypeNewValueChangeRule );
        assertEquals( "_name", rule.getName( "name", "_name" ) );
        assertEquals( 10, rule.getValue( "1", "int", "10" ) );
    }
}