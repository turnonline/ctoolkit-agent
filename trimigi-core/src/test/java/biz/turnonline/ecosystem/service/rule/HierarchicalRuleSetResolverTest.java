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

package biz.turnonline.ecosystem.service.rule;

import biz.turnonline.ecosystem.model.Export;
import biz.turnonline.ecosystem.model.api.MigrationSetRule;
import biz.turnonline.ecosystem.model.api.MigrationSetRuleGroup;
import com.google.gson.GsonBuilder;
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
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "and" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "Jack" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_AND_One()
    {
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "and" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_AND_All()
    {
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "and" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
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
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "or" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "Jack" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertFalse( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_OR_One()
    {
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "or" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
        propertyRuleSurname.setProperty( "surname" );
        propertyRuleSurname.setOperation( "eq" );
        propertyRuleSurname.setValue( "Bar" );
        ruleSet.getRules().add( propertyRuleSurname );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    @Test
    public void apply_OR_All()
    {
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "or" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        MigrationSetRule propertyRuleSurname = new MigrationSetRule();
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
        MigrationSetRuleGroup ruleSet = new MigrationSetRuleGroup();
        ruleSet.setOperation( "and" );

        MigrationSetRule propertyRuleName = new MigrationSetRule();
        propertyRuleName.setProperty( "name" );
        propertyRuleName.setOperation( "eq" );
        propertyRuleName.setValue( "John" );
        ruleSet.getRules().add( propertyRuleName );

        // nested rule set

        MigrationSetRuleGroup nestedRuleSet = new MigrationSetRuleGroup();
        nestedRuleSet.setOperation( "or" );
        propertyRuleName.setRuleGroups( nestedRuleSet );

        MigrationSetRule propertyRuleSurname1 = new MigrationSetRule();
        propertyRuleSurname1.setProperty( "surname" );
        propertyRuleSurname1.setOperation( "eq" );
        propertyRuleSurname1.setValue( "Foo" );
        nestedRuleSet.getRules().add( propertyRuleSurname1 );

        MigrationSetRule propertyRuleSurname2 = new MigrationSetRule();
        propertyRuleSurname2.setProperty( "surname" );
        propertyRuleSurname2.setOperation( "eq" );
        propertyRuleSurname2.setValue( "Bar" );
        nestedRuleSet.getRules().add( propertyRuleSurname2 );

        prettyPrint( ruleSet );

        assertTrue( resolver.apply( ruleSet, exportData() ) );
    }

    // -- private helpers

    public static Export exportData()
    {
        Export exportData = new Export();
        exportData.put( "name", "John" );
        exportData.put( "surname", "Foo" );

        return exportData;
    }

    private void prettyPrint( MigrationSetRuleGroup ruleSet )
    {
        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( ruleSet ) );
    }
}