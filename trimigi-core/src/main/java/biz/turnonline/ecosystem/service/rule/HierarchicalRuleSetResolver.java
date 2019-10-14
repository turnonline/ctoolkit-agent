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

import biz.turnonline.ecosystem.model.api.MigrationSetPropertyRuleSet;
import com.google.common.collect.Iterators;

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
    private static Map<RuleStrategy.Operation, RuleStrategy> ruleStrategies = new HashMap<>();

    static
    {
        ruleStrategies.put( RuleStrategy.Operation.EQ, MathOpsRuleStrategy.INSTANCE_EQ );
        ruleStrategies.put( RuleStrategy.Operation.LT, MathOpsRuleStrategy.INSTANCE_LT );
        ruleStrategies.put( RuleStrategy.Operation.LTE, MathOpsRuleStrategy.INSTANCE_LTE );
        ruleStrategies.put( RuleStrategy.Operation.GT, MathOpsRuleStrategy.INSTANCE_GT );
        ruleStrategies.put( RuleStrategy.Operation.GTE, MathOpsRuleStrategy.INSTANCE_GTE );
        ruleStrategies.put( RuleStrategy.Operation.REGEXP, RegexRuleStrategy.INSTANCE );
    }

    @Override
    @SuppressWarnings( "ConstantConditions" )
    public boolean apply( MigrationSetPropertyRuleSet ruleSet, Map<String, Object> ctx )
    {
        // by default allow entity ctx
        boolean apply = true;

        if ( ruleSet != null )
        {
            switch ( LogicalOperator.valueOf( ruleSet.getOperation().toUpperCase() ) )
            {
                case AND:
                {
                    apply = Iterators.all( ruleSet.getRules().iterator(), input -> {
                        RuleStrategy.Operation operation = RuleStrategy.Operation.get( input.getOperation() );
                        RuleStrategy strategy = ruleStrategies.get( operation );
                        boolean strategyApply = strategy.apply( input, ctx );

                        // recursive to support nested rules
                        if ( input.getRuleSet() != null )
                        {
                            return apply( input.getRuleSet(), ctx ) && strategyApply;
                        }

                        return strategyApply;
                    } );
                    break;
                }
                case OR:
                {
                    apply = Iterators.any( ruleSet.getRules().iterator(), input -> {
                        RuleStrategy.Operation operation = RuleStrategy.Operation.get( input.getOperation() );
                        RuleStrategy strategy = ruleStrategies.get( operation );
                        boolean strategyApply = strategy.apply( input, ctx );

                        // recursive to support nested rules
                        if ( input.getRuleSet() != null )
                        {
                            return apply( input.getRuleSet(), ctx ) || strategyApply;
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
