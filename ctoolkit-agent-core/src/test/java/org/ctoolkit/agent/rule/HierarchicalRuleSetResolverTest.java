/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.rule;

import com.google.gson.GsonBuilder;
import org.ctoolkit.agent.model.MigrationContext;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRuleSet;
import org.ctoolkit.agent.service.rule.HierarchicalRuleSetResolver;
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

    public static MigrationContext exportData()
    {
        MigrationContext exportData = new MigrationContext();
        exportData.put( "name", "John" );
        exportData.put( "surname", "Foo" );

        return exportData;
    }

    private void prettyPrint( MigrationSetPropertyRuleSet ruleSet )
    {
        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( ruleSet ) );
    }
}