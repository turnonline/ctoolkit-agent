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

import static biz.turnonline.ecosystem.Mocks.migrationContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link RuleStrategy}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RegexRuleStrategyTest
{
    private RuleStrategy strategy = RegexRuleStrategy.INSTANCE;

    @Test
    public void apply_NotString()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "^\\d(2)$" );

        assertTrue( strategy.apply( rule, new Export() ) );
    }

    @Test
    public void apply_String_NoMatch()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "\\d{2}" );

        assertFalse( strategy.apply( rule, migrationContext( "name", "John" ) ) );
    }

    @Test
    public void apply_String_Match()
    {
        MigrationSetRule rule = new MigrationSetRule();
        rule.setProperty( "name" );
        rule.setValue( "\\d{2}" );

        assertTrue( strategy.apply( rule, migrationContext( "name", "34" ) ) );
    }
}