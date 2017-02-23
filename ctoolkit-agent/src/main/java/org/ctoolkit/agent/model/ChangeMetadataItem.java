package org.ctoolkit.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Change metadata item entity
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Entity( name = "_ChangeMetadataItem" )
public class ChangeMetadataItem
        extends BaseMetadataItem<ChangeMetadata>
{
    public ChangeMetadataItem()
    {
    }

    public ChangeMetadataItem( ChangeMetadata metadata )
    {
        super( metadata );
    }

    @Override
    public String toString()
    {
        return "ChangeMetadataItem{} " + super.toString();
    }
}
