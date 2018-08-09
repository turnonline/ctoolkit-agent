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
