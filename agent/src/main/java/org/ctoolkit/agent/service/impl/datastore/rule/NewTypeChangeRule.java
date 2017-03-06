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

import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * <p>Change rule for use case:</p>
 * <code>newName == null && newType != null && newVal == null</code>
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class NewTypeChangeRule
        implements IChangeRule
{
    private EntityEncoder entityEncoder;

    @Inject
    public NewTypeChangeRule( EntityEncoder entityEncoder )
    {
        this.entityEncoder = entityEncoder;
    }

    @Override
    public boolean process( String newName, String newType, String newVal )
    {
        return newName == null && newType != null && newVal == null;
    }

    @Override
    public String getName( String originalName, String newName )
    {
        return originalName;
    }

    @Override
    public Object getValue( Object originalValue, String newType, String newValue )
    {
        String value = entityEncoder.encodeProperty( null, originalValue ).getValue();
        return entityEncoder.decodeProperty( newType, value );
    }
}
