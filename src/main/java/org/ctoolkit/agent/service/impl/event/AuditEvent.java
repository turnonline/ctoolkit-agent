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

package org.ctoolkit.agent.service.impl.event;

import org.ctoolkit.agent.model.Action;
import org.ctoolkit.agent.model.BaseEntity;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.model.Operation;

import java.util.HashMap;
import java.util.Map;

/**
 * Audit event is posted when changeset operation occurs
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuditEvent
{
    private static Map<Class, Operation> operationMap = new HashMap<>();

    static
    {
        operationMap.put( ImportMetadata.class, Operation.IMPORT );
        operationMap.put( ImportMetadataItem.class, Operation.IMPORT_ITEM );
        operationMap.put( ExportMetadata.class, Operation.EXPORT );
        operationMap.put( ExportMetadataItem.class, Operation.EXPORT_ITEM );
        operationMap.put( MigrationMetadata.class, Operation.MIGRATION );
        operationMap.put( MigrationMetadataItem.class, Operation.MIGRATION_ITEM );
    }

    private Operation operation;

    private Action action;

    private String owner;

    public AuditEvent()
    {
    }

    public AuditEvent( Action action, BaseEntity owner )
    {
        this.operation = operationMap.get( owner.getClass() );
        this.action = action;
        this.owner = owner instanceof BaseMetadataItem ? owner.key().getParent().getId().toString() : owner.key().getId().toString();
    }

    public Operation getOperation()
    {
        return operation;
    }

    public Action getAction()
    {
        return action;
    }

    @SuppressWarnings( "unchecked" )
    public String getOwner()
    {
        return owner;
    }

    @Override
    public String toString()
    {
        return "AuditEvent{" +
                "operation=" + operation +
                ", action=" + action +
                ", owner=" + owner +
                "} " + super.toString();
    }
}