package org.ctoolkit.agent.rule;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetPropertyRule;

import java.util.Arrays;

/**
 * API for algebraic strategies('=', '>', '>=', '<', '<='), regular expression, etc...
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface RuleSetStrategy
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

            return null;
        }
    }

    /**
     * Return <code>true</code> if exported entity should by migrated by provide rule, <code>false</code> otherwise
     *
     * @param rule             {@link MigrationSetPropertyRule} containing rule set operation ('=', '>', '<=', regexp, etc.)
     * @param entityExportData {@link EntityExportData} contains values for rule decision logic
     * @return <code>true</code> if exported entity should be migrated
     */
    boolean apply( MigrationSetPropertyRule rule, EntityExportData entityExportData );
}
