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

package org.ctoolkit.agent.service.rule;

import org.ctoolkit.agent.model.MigrationContext;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;

/**
 * Rule set strategy for regular expression
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RegexRuleStrategy
        implements RuleStrategy
{
    public static final RuleStrategy INSTANCE = new RegexRuleStrategy();

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean apply( MigrationSetPropertyRule rule, MigrationContext migrationContext )
    {
//        MigrationContext.Property property = migrationContext.getProperties().get( rule.getProperty() );
//        String ruleValue = rule.getValue();
//
//        if ( property != null && property.getValue() instanceof String )
//        {
//            Pattern pattern = Pattern.compile( ruleValue );
//            Matcher matcher = pattern.matcher( ( String ) property.getValue() );
//            return matcher.matches();
//        }

        // return true if property was not found - it means that we do not want to filter row if property is not found
        return true;
    }
}
