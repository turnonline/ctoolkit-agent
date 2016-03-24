package org.ctoolkit.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Change metadata entity.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity( name = "_ChangeMetadata" )
public class ChangeMetadata
        extends BaseMetadata<ChangeMetadataItem>
{
    @Override
    protected ChangeMetadataItem newItem()
    {
        return new ChangeMetadataItem( this );
    }

    @Override
    public String toString()
    {
        return "ChangeMetadata{} " + super.toString();
    }
}
