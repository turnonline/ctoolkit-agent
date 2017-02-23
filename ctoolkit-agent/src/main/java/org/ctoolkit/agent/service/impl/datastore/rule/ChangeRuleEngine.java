package org.ctoolkit.agent.service.impl.datastore.rule;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Change rule engine to determine database new property name and value
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeRuleEngine
{
    private List<IChangeRule> rules = new ArrayList<>();

    @Inject
    public ChangeRuleEngine( NewNameChangeRule newNameChangeRule,
                             NewTypeChangeRule newTypeChangeRule,
                             NewValueChangeRule newValueChangeRule,
                             NewNameNewTypeChangeRule newNameNewTypeChangeRule,
                             NewNameNewValueChangeRule newNameNewValueChangeRule,
                             NewTypeNewValueChangeRule newTypeNewValueChangeRule,
                             NewNameNewTypeNewValueChangeRule newNameNewTypeNewValueChangeRule )
    {
        rules.add( newNameChangeRule );
        rules.add( newTypeChangeRule );
        rules.add( newValueChangeRule );
        rules.add( newNameNewTypeChangeRule );
        rules.add( newNameNewValueChangeRule );
        rules.add( newTypeNewValueChangeRule );
        rules.add( newNameNewTypeNewValueChangeRule );
    }

    public IChangeRule provideRule( String newName, String newType, String newVal )
    {
        for ( IChangeRule rule : rules )
        {
            if ( rule.process( newName, newType, newVal ) )
            {
                return rule;
            }
        }

        throw new IllegalArgumentException( "No rule defined!" );
    }
}
