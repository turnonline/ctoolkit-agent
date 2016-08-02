package org.ctoolkit.migration.service.impl.datastore.rule;

import org.ctoolkit.migration.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * <p>Change rule for use case:</p>
 * <code>newName == null && newType != null && newVal == null</code>
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class NewTypeChangeRule implements IChangeRule
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
