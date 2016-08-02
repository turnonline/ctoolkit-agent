package org.ctoolkit.migration.service.impl.datastore.rule;

/**
 * <p>Change rule for use case:</p>
 * <code>newName != null && newType == null && newVal == null</code>
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class NewNameChangeRule
        implements IChangeRule
{
    @Override
    public boolean process( String newName, String newType, String newVal )
    {
        return newName != null && newType == null && newVal == null;
    }

    @Override
    public String getName( String originalName, String newName )
    {
        return newName;
    }

    @Override
    public Object getValue( Object originalValue, String newType, String newValue )
    {
        return originalValue;
    }
}
