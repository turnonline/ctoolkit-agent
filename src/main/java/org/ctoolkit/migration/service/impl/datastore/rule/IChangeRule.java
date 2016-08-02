package org.ctoolkit.migration.service.impl.datastore.rule;

/**
 * API for change rule definition
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface IChangeRule
{
    boolean process( String newName, String newType, String newVal );

    String getName( String originalName, String newName );

    Object getValue( Object originalValue, String newType, String newValue );
}
