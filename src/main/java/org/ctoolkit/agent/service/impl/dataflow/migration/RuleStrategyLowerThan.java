package org.ctoolkit.agent.service.impl.dataflow.migration;

import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * Rule strategy for lower than (a < b) operation
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class RuleStrategyLowerThan
        extends RuleStrategyMathOpBase
{
    @Inject
    public RuleStrategyLowerThan( EntityEncoder encoder )
    {
        super( encoder );
    }

    @Override
    protected boolean apply( Double entityProperty, Double ruleProperty )
    {
        return entityProperty < ruleProperty;
    }

    @Override
    protected boolean apply( Long entityProperty, Long ruleProperty )
    {
        return entityProperty < ruleProperty;
    }
}
