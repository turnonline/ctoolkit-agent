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
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import static biz.turnonline.ecosystem.Mocks.migrationContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link MathOpsRuleStrategy}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class MathOpsGreaterThanEqualsRuleStrategyTest
{
    private RuleStrategy strategy = MathOpsRuleStrategy.INSTANCE_GTE;

    @Test
    public void apply_PropertyNotFound()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, new Export() ) );
    }

    // -- String

    @Test
    public void apply_StringLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, migrationContext( "name", "Joh" ) ) );
    }

    @Test
    public void apply_StringEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, migrationContext( "name", "John" ) ) );
    }

    @Test
    public void apply_StringGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, migrationContext( "name", "Johny" ) ) );
    }

    // -- Integer

    @Test
    public void apply_IntegerLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 29 ) ) );
    }

    @Test
    public void apply_IntegerEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30 ) ) );
    }

    @Test
    public void apply_IntegerGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 31 ) ) );
    }

    // -- Long

    @Test
    public void apply_LongLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 29L ) ) );
    }

    @Test
    public void apply_LongEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30L ) ) );
    }

    @Test
    public void apply_LongGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 31L ) ) );
    }

    // -- Float

    @Test
    public void apply_FloatLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 29F ) ) );
    }

    @Test
    public void apply_FloatEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30F ) ) );
    }

    @Test
    public void apply_FloatGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 31F ) ) );
    }

    // -- Double

    @Test
    public void apply_DoubleLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 29D ) ) );
    }

    @Test
    public void apply_DoubleEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30D ) ) );
    }

    @Test
    public void apply_DoubleGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 31D ) ) );
    }

    // -- BigDecimal

    @Test
    public void apply_BigDecimalLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", BigDecimal.valueOf( 29 ) ) ) );
    }

    @Test
    public void apply_BigDecimalEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", BigDecimal.valueOf( 30 ) ) ) );
    }

    @Test
    public void apply_BigDecimalGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", BigDecimal.valueOf( 31 ) ) ) );
    }

    // -- Boolean

    @Test
    public void apply_BooleanLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "active" );
        rule.setValue( "true" );

        assertFalse( strategy.apply( rule, migrationContext( "active", false ) ) );
    }

    @Test
    public void apply_BooleanEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "active" );
        rule.setValue( "true" );

        assertTrue( strategy.apply( rule, migrationContext( "active", true ) ) );
    }

    // -- Date

    @Test
    public void apply_DateLower()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764900000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertFalse( strategy.apply( rule, migrationContext( "createDate", calendar.getTime() ) ) );
    }

    @Test
    public void apply_DateEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764800000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertTrue( strategy.apply( rule, migrationContext( "createDate", calendar.getTime() ) ) );
    }

    @Test
    public void apply_DateGreater()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1514764600000" );

        Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 2018, Calendar.JANUARY, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        assertTrue( strategy.apply( rule, migrationContext( "createDate", calendar.getTime() ) ) );
    }
}