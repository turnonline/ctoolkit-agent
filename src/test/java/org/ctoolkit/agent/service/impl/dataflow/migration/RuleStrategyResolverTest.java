package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.exception.RuleStrategyException;
import org.ctoolkit.agent.resource.MigrationSetKindOpRule;
import org.ctoolkit.agent.resource.MigrationSetKindOpRuleSet;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit test for {@link RuleStrategyResolver}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyResolverTest
{
    private EntityEncoder encoder = new EntityEncoder();

    private RuleStrategyEquals ruleStrategyEquals = new RuleStrategyEquals( encoder );
    private RuleStrategyLowerThan ruleStrategyLowerThan = new RuleStrategyLowerThan( encoder );
    private RuleStrategyLowerThanEquals ruleStrategyLowerThanEquals = new RuleStrategyLowerThanEquals( encoder );
    private RuleStrategyGreaterThan ruleStrategyGreaterThan = new RuleStrategyGreaterThan( encoder );
    private RuleStrategyGreaterThanEquals ruleStrategyGreaterThanEquals = new RuleStrategyGreaterThanEquals( encoder );
    private RuleStrategyRegexp ruleStrategyRegexp = new RuleStrategyRegexp( encoder );

    private RuleStrategyResolver resolver;

    @Before
    public void setUp() throws Exception
    {
        resolver = new RuleStrategyResolver(
                ruleStrategyEquals,
                ruleStrategyLowerThan,
                ruleStrategyLowerThanEquals,
                ruleStrategyGreaterThan,
                ruleStrategyGreaterThanEquals,
                ruleStrategyRegexp
        );
    }

    // -- resolver tests

    @Test
    public void testRuleSetIsNull()
    {
        assertTrue( resolver.apply( null, null ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testRuleSetUnknownOperation()
    {
        MigrationSetKindOpRuleSet ruleSet = new MigrationSetKindOpRuleSet();
        ruleSet.setOperation( "xor" );

        try
        {
            resolver.apply( ruleSet, null );
        }
        catch ( IllegalArgumentException e )
        {
            assertTrue( e.getMessage().startsWith( "Unknown operation for rule set" ) );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleSetUnknownStrategy()
    {
        MigrationSetKindOpRuleSet ruleSet = new MigrationSetKindOpRuleSet();
        ruleSet.getRules().add( new MigrationSetKindOpRule() );
        ruleSet.getRules().get( 0 ).setOperation( "isEquals" );

        try
        {
            resolver.apply( ruleSet, null );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "No rule strategy exists for rule operation: MigrationSetKindOpRule{property='null', operation='isEquals', value='null'}. Allowed operations are: [eq, lt, lte, gt, gte, regexp] but actual operation is isEquals", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleSetNoneAllTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.setOperation( null ); // set operation to null - resolver should handle it as and
        Entity entity = createEntityBuilder().build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetAndAllTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        Entity entity = createEntityBuilder().build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetAndOneTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "Mike" );

        Entity entity = createEntityBuilder().build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetAndNoneTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "Mike" );
        ruleSet.getRules().get( 1 ).setValue( "Bar" );

        Entity entity = createEntityBuilder().build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetOrAllTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.setOperation( MigrationSetKindOpRuleSet.OR );

        Entity entity = createEntityBuilder().build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetOrOneTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.setOperation( MigrationSetKindOpRuleSet.OR );
        ruleSet.getRules().get( 0 ).setValue( "Mike" );

        Entity entity = createEntityBuilder().build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleSetOrNoneTrue()
    {
        MigrationSetKindOpRuleSet ruleSet = createComplexRuleSet();
        ruleSet.setOperation( MigrationSetKindOpRuleSet.OR );
        ruleSet.getRules().get( 0 ).setValue( "Mike" );
        ruleSet.getRules().get( 1 ).setValue( "Bar" );

        Entity entity = createEntityBuilder().build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    // -- RuleStrategyEquals tests

    @Test
    public void testRuleStrategyEquals_String_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new StringValue( "John" ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_String_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new StringValue( "Mike" ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Double_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Double_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        Entity entity = createEntityBuilder( new DoubleValue( 2L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Long_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Long_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        Entity entity = createEntityBuilder( new LongValue( 2L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Boolean_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "true" );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Boolean_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "false" );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Null_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "null" );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Null_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new NullValue() ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Key_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "Person:1" );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyEquals_Key_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "Person:2" );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyEquals_Date()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='eq', value='John'}. Allowed types are: [string, double, long, boolean, null, reference] but actual entity property 'prop' is type of date.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyEquals_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='eq', value='John'}. Allowed types are: [string, double, long, boolean, null, reference] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyEquals_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='eq', value='John'}. Allowed types are: [string, double, long, boolean, null, reference] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyEquals_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "Foo" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='eq', value='John'}. Allowed types are: [string, double, long, boolean, null, reference] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyEquals_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='eq', value='John'}. Allowed types are: [string, double, long, boolean, null, reference] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // -- RuleStrategyLowerThan tests

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_String()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new StringValue( "John" ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyLowerThan_Double_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Double_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Double_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Long_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Long_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Long_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_Boolean()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of boolean.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_Null()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of null.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_Key()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyLowerThan_Date_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Date_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122907" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThan_Date_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122900" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "John" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThan_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // -- RuleStrategyLowerThanEquals tests

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_String()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new StringValue( "John" ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Double_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Double_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Double_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Long_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Long_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Long_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_Boolean()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of boolean.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_Null()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of null.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_Key()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Date_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Date_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122910L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyLowerThanEquals_Date_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122900" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "John" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyLowerThanEquals_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.LOWER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='lte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // -- RuleStrategyGreaterThan tests

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_String()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new StringValue( "John" ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyGreaterThan_Double_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Double_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Double_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Long_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Long_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Long_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_Boolean()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of boolean.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_Null()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of null.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_Key()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyGreaterThan_Date_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122900" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Date_False1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122910L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThan_Date_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "John" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThan_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN );
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gt', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // -- RuleStrategyGreaterThanEquals tests

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_String()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new StringValue( "John" ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Double_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Double_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Double_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Long_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "0" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Long_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Long_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "2" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_Boolean()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of boolean.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_Null()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of null.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_Key()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference.", e.getMessage() );
            throw e;
        }
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Date_True1()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122900" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Date_True2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122910L ) ) ) ).build();

        assertTrue( resolver.apply( ruleSet, entity ) );
    }

    @Test
    public void testRuleStrategyGreaterThanEquals_Date_False2()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setValue( "1499287122910" );
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        assertFalse( resolver.apply( ruleSet, entity ) );
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "John" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyGreaterThanEquals_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.GREATER_THAN_EQUALS );
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='gte', value='John'}. Allowed types are: [double, long, date] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // -- RuleStrategyRegexp tests

    @Test
    public void testRuleStrategyRegexp_String_True()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        ruleSet.getRules().get( 0 ).setValue( "^\\d[2]foo" );
        Entity entity = createEntityBuilder( new StringValue( "12foo" ) ).build();

        boolean apply = resolver.apply( ruleSet, entity );
        assertTrue( apply );
    }

    @Test
    public void testRuleStrategyRegexp_String_False()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        ruleSet.getRules().get( 0 ).setValue( "^\\d[2]foo" );
        Entity entity = createEntityBuilder( new StringValue( "1foo" ) ).build();

        boolean apply = resolver.apply( ruleSet, entity );
        assertFalse( apply );
    }

    @Test(expected = RuleStrategyException.class)
    public void testRuleStrategyRegexp_Double()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new DoubleValue( 1L ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of double.", e.getMessage() );
            throw e;
        }
    }

    @Test(expected = RuleStrategyException.class)
    public void testRuleStrategyRegexp_Long()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new LongValue( 1L ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of long.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_Boolean()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new BooleanValue( true ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of boolean.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_Null()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new NullValue() ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of null.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_Key()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of reference.", e.getMessage() );
            throw e;
        }
    }

    @Test(expected = RuleStrategyException.class)
    public void testRuleStrategyRegexp_Date()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new TimestampValue( Timestamp.of( new Date( 1499287122907L ) ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of date.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_Blob()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new BlobValue( Blob.copyFrom( new byte[]{} ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of blob.", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_ListKey()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new ListValue( new KeyValue( Key.newBuilder( "c-toolkit", "Person", 1 ).build() ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of reference (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_ListString()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new ListValue( new StringValue( "John" ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of string (list).", e.getMessage() );
            throw e;
        }
    }

    @Test( expected = RuleStrategyException.class )
    public void testRuleStrategyRegexp_ListLong()
    {
        MigrationSetKindOpRuleSet ruleSet = createSimpleRuleSet();
        ruleSet.getRules().get( 0 ).setOperation( MigrationSetKindOpRule.REGEXP );
        Entity entity = createEntityBuilder( new ListValue( new LongValue( 1L ) ) ).build();

        try
        {
            resolver.apply( ruleSet, entity );
            fail( RuleStrategyException.class.getName() + " expected!" );
        }
        catch ( RuleStrategyException e )
        {
            assertEquals( "Type is not allowed for rule: MigrationSetKindOpRule{property='prop', operation='regexp', value='John'}. Allowed types are: [string] but actual entity property 'prop' is type of long (list).", e.getMessage() );
            throw e;
        }
    }

    // ------------------
    // -- private helpers
    // ------------------

    private MigrationSetKindOpRuleSet createComplexRuleSet()
    {
        MigrationSetKindOpRule rule1 = new MigrationSetKindOpRule();
        rule1.setOperation( MigrationSetKindOpRule.EQUALS );
        rule1.setProperty( "name" );
        rule1.setValue( "John" );

        MigrationSetKindOpRule rule2 = new MigrationSetKindOpRule();
        rule2.setOperation( MigrationSetKindOpRule.EQUALS );
        rule2.setProperty( "surname" );
        rule2.setValue( "Foo" );

        MigrationSetKindOpRuleSet ruleSet = new MigrationSetKindOpRuleSet();
        ruleSet.setOperation( MigrationSetKindOpRuleSet.AND );
        ruleSet.getRules().add( rule1 );
        ruleSet.getRules().add( rule2 );

        return ruleSet;
    }

    private MigrationSetKindOpRuleSet createSimpleRuleSet()
    {
        MigrationSetKindOpRule rule1 = new MigrationSetKindOpRule();
        rule1.setOperation( MigrationSetKindOpRule.EQUALS );
        rule1.setProperty( "prop" );
        rule1.setValue( "John" );

        MigrationSetKindOpRuleSet ruleSet = new MigrationSetKindOpRuleSet();
        ruleSet.getRules().add( rule1 );

        return ruleSet;
    }

    private Entity.Builder createEntityBuilder()
    {
        return Entity
                .newBuilder( Key.newBuilder( "c-toolkit", "Person", 1 ).build() )
                .set( "name", "John" )
                .set( "surname", "Foo" );
    }

    private Entity.Builder createEntityBuilder( Value<?> value )
    {
        return Entity
                .newBuilder( Key.newBuilder( "c-toolkit", "Person", 1 ).build() )
                .set( "prop", value );
    }
}