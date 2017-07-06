package org.ctoolkit.agent.config;

import com.google.inject.AbstractModule;
import org.ctoolkit.agent.service.impl.dataflow.migration.Add__NewName_NewType_NewValue;
import org.ctoolkit.agent.service.impl.dataflow.migration.Add__NewName_NewType_NewValueNull;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewName;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewName_NewType;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewName_NewType_NewValue;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewName_NewValue;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewType;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewType_NewValue;
import org.ctoolkit.agent.service.impl.dataflow.migration.Change__NewValue;
import org.ctoolkit.agent.service.impl.dataflow.migration.IRuleStrategyResolver;
import org.ctoolkit.agent.service.impl.dataflow.migration.Remove__Kind;
import org.ctoolkit.agent.service.impl.dataflow.migration.Remove__Property;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyEquals;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyGreaterThan;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyGreaterThanEquals;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyLowerThan;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyLowerThanEquals;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyRegexp;
import org.ctoolkit.agent.service.impl.dataflow.migration.RuleStrategyResolver;
import org.ctoolkit.agent.service.impl.dataflow.migration.UseCaseResolver;

import javax.inject.Singleton;

/**
 * Separate guice module for migration
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrationModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        // use cases for operations
        bind( UseCaseResolver.class ).in( Singleton.class );

        bind( Add__NewName_NewType_NewValue.class ).in( Singleton.class );
        bind( Add__NewName_NewType_NewValueNull.class ).in( Singleton.class );

        bind( Remove__Kind.class ).in( Singleton.class );
        bind( Remove__Property.class ).in( Singleton.class );

        bind( Change__NewName.class ).in( Singleton.class );
        bind( Change__NewType.class ).in( Singleton.class );
        bind( Change__NewValue.class ).in( Singleton.class );

        bind( Change__NewName_NewType.class ).in( Singleton.class );
        bind( Change__NewName_NewValue.class ).in( Singleton.class );
        bind( Change__NewType_NewValue.class ).in( Singleton.class );

        bind( Change__NewName_NewType_NewValue.class ).in( Singleton.class );

        // strategies for rule set
        bind( IRuleStrategyResolver.class ).to( RuleStrategyResolver.class ).in( Singleton.class );

        bind( RuleStrategyEquals.class ).in( Singleton.class );
        bind( RuleStrategyLowerThan.class ).in( Singleton.class );
        bind( RuleStrategyLowerThanEquals.class ).in( Singleton.class );
        bind( RuleStrategyGreaterThan.class ).in( Singleton.class );
        bind( RuleStrategyGreaterThanEquals.class ).in( Singleton.class );
        bind( RuleStrategyRegexp.class ).in( Singleton.class );
    }
}
