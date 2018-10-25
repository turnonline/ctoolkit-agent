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

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRuleSet;

/**
 * API for rule set resolving
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface RuleSetResolver
{
    enum LogicalOperator
    {
        AND,
        OR
    }

    /**
     * Return <code>true</code> if exported entity should by migrated by provide rule set, <code>false</code> otherwise
     *
     * @param ruleSet          {@link MigrationSetPropertyRule} containing logical operations (and, or) and mathematical operations ('=', '>', '<=', regexp, etc.)
     * @param entityExportData {@link EntityExportData} contains values for rule decision logic
     * @return <code>true</code> if exported entity should be migrated
     */
    boolean apply( MigrationSetPropertyRuleSet ruleSet, EntityExportData entityExportData );
}
