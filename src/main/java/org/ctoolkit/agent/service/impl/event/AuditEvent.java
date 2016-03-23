package org.ctoolkit.agent.service.impl.event;

import java.util.HashMap;

/**
 * Audit event is posted when changeset operation occurs
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AuditEvent
        extends HashMap<String, String>
{
    public enum Phase
    {
        BEFORE,
        AFTER
    }

    public enum Action
    {
        ADD_ENTITY,
        CLEAR_ENTITY,
        DROP_ENTITY,

        ADD_ENTITY_PROPERTY,
        CHANGE_ENTITY_PROPERTY,
        REMOVE_ENTITY_PROPERTY
    }

    public enum Operation
    {
        IMPORT,
        EXPORT,
        CHANGE
    }

    private Operation operation;

    private Action action;

    private Phase phase;

    public AuditEvent( )
    {
    }

    public AuditEvent( Operation operation, Action action, Phase phase )
    {
        this.operation = operation;
        this.action = action;
        this.phase = phase;
    }

    public Operation getOperation()
    {
        return operation;
    }

    public Action getAction()
    {
        return action;
    }

    public Phase getPhase()
    {
        return phase;
    }

    @Override
    public String toString()
    {
        return "AuditEvent{" +
                "operation=" + operation +
                ", action=" + action +
                ", phase=" + phase +
                "} " + super.toString();
    }
}
