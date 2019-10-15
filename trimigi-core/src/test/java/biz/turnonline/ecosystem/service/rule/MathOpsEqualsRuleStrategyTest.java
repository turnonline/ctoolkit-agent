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
public class MathOpsEqualsRuleStrategyTest
{
    private RuleStrategy strategy = MathOpsRuleStrategy.INSTANCE_EQ;

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
    public void apply_StringNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, migrationContext( "name", "Jack" ) ) );
    }

    @Test
    public void apply_StringEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, migrationContext( "name", "John" ) ) );
    }

    // -- Integer

    @Test
    public void apply_IntegerNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 31 ) ) );
    }

    @Test
    public void apply_IntegerEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30 ) ) );
    }

    // -- Long

    @Test
    public void apply_LongNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 31L ) ) );
    }

    @Test
    public void apply_LongEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30L ) ) );
    }

    // -- Float

    @Test
    public void apply_FloatNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 31F ) ) );
    }

    @Test
    public void apply_FloatEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30F ) ) );
    }

    // -- Double

    @Test
    public void apply_DoubleNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", 31D ) ) );
    }

    @Test
    public void apply_DoubleEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", 30D ) ) );
    }

    // -- BigDecimal

    @Test
    public void apply_BigDecimalNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertFalse( strategy.apply( rule, migrationContext( "age", BigDecimal.valueOf( 31 ) ) ) );
    }

    @Test
    public void apply_BigDecimalEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "age" );
        rule.setValue( "30" );

        assertTrue( strategy.apply( rule, migrationContext( "age", BigDecimal.valueOf( 30 ) ) ) );
    }

    // -- Boolean

    @Test
    public void apply_BooleanNotEquals()
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

    // -- Byte array

    @Test
    public void apply_ByteArrayNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "data" );
        rule.setValue( "John" );

        assertFalse( strategy.apply( rule, migrationContext( "data", new byte[]{'j'} ) ) );
    }

    @Test
    public void apply_ByteArrayEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "active" );
        rule.setValue( "John" );

        assertTrue( strategy.apply( rule, migrationContext( "data", new byte[]{'J', 'o', 'h', 'n'} ) ) );
    }

    // -- Date

    @Test
    public void apply_DateNotEquals()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "createDate" );
        rule.setValue( "1" );

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
}