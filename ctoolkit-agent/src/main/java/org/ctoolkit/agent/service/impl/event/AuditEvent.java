package org.ctoolkit.agent.service.impl.event;

import org.ctoolkit.agent.model.BaseEntity;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.MetadataAudit.Action;
import org.ctoolkit.agent.model.MetadataAudit.Operation;

import java.util.HashMap;
import java.util.Map;

/**
 * Audit event is posted when changeset operation occurs
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuditEvent
        extends HashMap<String, String>
{
    private static Map<Class, Operation> operationMap = new HashMap<>();

    static
    {
        operationMap.put( ImportMetadata.class, Operation.IMPORT );
        operationMap.put( ImportMetadataItem.class, Operation.IMPORT_ITEM );
        operationMap.put( ExportMetadata.class, Operation.EXPORT );
        operationMap.put( ExportMetadataItem.class, Operation.EXPORT_ITEM );
        operationMap.put( ChangeMetadata.class, Operation.CHANGE );
        operationMap.put( ChangeMetadataItem.class, Operation.CHANGE_ITEM );
    }

    private Operation operation;

    private Action action;

    private BaseEntity owner;

    public AuditEvent()
    {
    }

    public AuditEvent( Action action, BaseEntity owner )
    {
        this.operation = operationMap.get( owner.getClass() );
        this.action = action;
        this.owner = owner;
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
    public BaseEntity getOwner()
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