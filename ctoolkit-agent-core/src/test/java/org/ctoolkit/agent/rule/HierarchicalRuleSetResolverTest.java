package org.ctoolkit.agent.rule;

import com.google.gson.GsonBuilder;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRuleSet;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link HierarchicalRuleSetResolver}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class HierarchicalRuleSetResolverTest
{

    private HierarchicalRuleSetResolver resolver = new HierarchicalRuleSetResolver();

    // -- AND

    @Test
    public void apply_RuleSet_Null()
    {
        assertTrue( resolver.apply( null, exportData() ) );
    }

    // -- AND

    @Test
    public void apply_AND_None()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "and" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "Jack" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_AND_One()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "and" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_AND_All()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "and" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Foo" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    // -- OR

    @Test
    public void apply_OR_None()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "or" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "Jack" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_OR_One()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "or" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_OR_All()
    {
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "or" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetPropertyRule propertyRuleSurname = new MigrationSetPropertyRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Foo" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    // -- nested

    @Test
    public void apply_Nested_All() // name='John' AND (surname='Foo' OR surname='Bar')
    {
        // root rule set
        MigrationSetPropertyRuleSet ruleSet = new MigrationSetPropertyRuleSet();
        ruleSet.setOperation( "and" );

        MigrationSetPropertyRule propertyRuleName = new MigrationSetPropertyRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        // nested rule set

        MigrationSetPropertyRuleSet nestedRuleSet = new MigrationSetPropertyRuleSet();
        nestedRuleSet.setOperation( "or" );
        propertyRuleName.setRuleSet( nestedRuleSet );

        MigrationSetPropertyRule propertyRuleSurname1 = new MigrationSetPropertyRule();
        propertyRuleSurname1.setProperty( "surname" );
        propertyRuleSurname1.setOperation( "eq" );
        propertyRuleSurname1.setValue( "Foo" );
        nestedRuleSet.getRules().add( propertyRuleSurname1 );

        MigrationSetPropertyRule propertyRuleSurname2 = new MigrationSetPropertyRule();
        propertyRuleSurname2.setProperty( "surname" );
        propertyRuleSurname2.setOperation( "eq" );
        propertyRuleSurname2.setValue( "Bar" );
        nestedRuleSet.getRules().add( propertyRuleSurname2 );

        prettyPrint( ruleSet );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    // -- private helpers

    public static EntityExportData exportData()
    {
        EntityExportData exportData = new EntityExportData();
        exportData.getProperties().put( "name", new EntityExportData.Property( "John" ) );
        exportData.getProperties().put( "surname", new EntityExportData.Property( "Foo" ) );

        return exportData;
    }

    private void prettyPrint( MigrationSetPropertyRuleSet ruleSet )
    {
        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( ruleSet ) );
    }
}