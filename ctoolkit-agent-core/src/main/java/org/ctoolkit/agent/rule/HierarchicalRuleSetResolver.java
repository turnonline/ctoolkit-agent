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

import com.google.common.collect.Iterators;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRuleSet;
import org.ctoolkit.agent.rule.RuleStrategy.Operation;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Hierarchical rule resolver. It support nested rules i.e.: name='John' AND (surname='Foo' OR surname='Bar')
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class HierarchicalRuleSetResolver
        implements RuleSetResolver
{
    private static Map<Operation, RuleStrategy> ruleStrategies = new HashMap<>();

    static
    {
        ruleStrategies.put( Operation.EQ, MathOpsRuleStrategy.INSTANCE_EQ );
        ruleStrategies.put( Operation.LT, MathOpsRuleStrategy.INSTANCE_LT );
        ruleStrategies.put( Operation.LTE, MathOpsRuleStrategy.INSTANCE_LTE );
        ruleStrategies.put( Operation.GT, MathOpsRuleStrategy.INSTANCE_GT );
        ruleStrategies.put( Operation.GTE, MathOpsRuleStrategy.INSTANCE_GTE );
        ruleStrategies.put( Operation.REGEXP, RegexRuleStrategy.INSTANCE );
    }

    @Override
    @SuppressWarnings( "ConstantConditions" )
    public boolean apply( MigrationSetPropertyRuleSet ruleSet, EntityExportData entityExportData )
    {
        // by default allow entity export
        boolean apply = true;

        if ( ruleSet != null )
        {
            switch ( LogicalOperator.valueOf( ruleSet.getOperation().toUpperCase() ) )
            {
                case AND:
                {
                    apply = Iterators.all( ruleSet.getRules().iterator(), input -> {
                        Operation operation = Operation.get( input.getOperation() );
                        RuleStrategy strategy = ruleStrategies.get( operation );
                        boolean strategyApply = strategy.apply( input, entityExportData );

                        // recursive to support nested rules
                        if ( input.getRuleSet() != null )
                        {
                            return apply( input.getRuleSet(), entityExportData ) && strategyApply;
                        }

                        return strategyApply;
                    } );
                    break;
                }
                case OR:
                {
                    apply = Iterators.any( ruleSet.getRules().iterator(), input -> {
                        Operation operation = Operation.get( input.getOperation() );
                        RuleStrategy strategy = ruleStrategies.get( operation );
                        boolean strategyApply = strategy.apply( input, entityExportData );

                        // recursive to support nested rules
                        if ( input.getRuleSet() != null )
                        {
                            return apply( input.getRuleSet(), entityExportData ) || strategyApply;
                        }

                        return strategyApply;
                    } );
                    break;
                }
            }
        }

        return apply;
    }
}
