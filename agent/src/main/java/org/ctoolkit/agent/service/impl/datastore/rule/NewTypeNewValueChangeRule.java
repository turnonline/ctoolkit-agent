package org.ctoolkit.agent.service.impl.datastore.rule;

import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;

import javax.inject.Inject;

/**
 * <p>Change rule for use case:</p>
 * <code>newName == null && newType != null && newVal != null</code>
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class NewTypeNewValueChangeRule
        implements IChangeRule
{
    private EntityEncoder entityEncoder;

    @Inject
    public NewTypeNewValueChangeRule( EntityEncoder entityEncoder )
    {
        this.entityEncoder = entityEncoder;
    }

    @Override
    public boolean process( String newName, String newType, String newVal )
    {
        return newName == null && newType != null && newVal != null;
    }

    @Override
    public String getName( String originalName, String newName )
    {
        return originalName;
    }

    @Override
    public Object getValue( Object originalValue, String newType, String newValue )
    {
        return entityEncoder.decodeProperty( newType, newValue );
    }
}
