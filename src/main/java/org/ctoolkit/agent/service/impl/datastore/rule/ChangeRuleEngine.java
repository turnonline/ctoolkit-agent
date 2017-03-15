/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

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
