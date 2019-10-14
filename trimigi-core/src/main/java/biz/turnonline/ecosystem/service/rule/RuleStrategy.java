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

import biz.turnonline.ecosystem.model.api.MigrationSetPropertyRule;

import java.util.Arrays;
import java.util.Map;

/**
 * API for algebraic strategies {@code ('=', '>', '>=', '<', '<=')}, regular expression, etc...
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface RuleStrategy
{
    enum Operation
    {
        EQ( new String[]{"eq", "="} ),
        LT( new String[]{"lt", "<"} ),
        LTE( new String[]{"lte", "<="} ),
        GT( new String[]{"gt", ">"} ),
        GTE( new String[]{"gte", ">="} ),
        REGEXP( new String[]{"regexp"} );

        private String[] values;

        Operation( String[] values )
        {
            this.values = values;
        }

        public static Operation get( String value )
        {
            for ( Operation operation : Operation.values() )
            {
                if ( Arrays.asList( operation.values ).contains( value ) )
                {
                    return operation;
                }
            }

            throw new IllegalArgumentException( "Operation '" + value + "' not supported. Supported operations are: '=', 'eq', '<', 'lt', '<=', 'lte', '>', 'gt', '>=', 'gte', 'regexp'." );
        }
    }

    /**
     * Return <code>true</code> if exported entity should by migrated by provide rule, <code>false</code> otherwise
     *
     * @param rule             {@link MigrationSetPropertyRule} containing rule set operation {@code ('=', '>', '<=', regexp, etc.)}
     * @param ctx {@link Map} contains values for rule decision logic
     * @return <code>true</code> if exported entity should be migrated
     */
    boolean apply( MigrationSetPropertyRule rule, Map<String, Object> ctx );
}
