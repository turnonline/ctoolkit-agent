package org.ctoolkit.agent.service.impl.datastore.rule;

/**
 * API for change rule definition
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface IChangeRule
{
    boolean process( String newName, String newType, String newVal );

    String getName( String originalName, String newName );

    Object getValue( Object originalValue, String newType, String newValue );
}
