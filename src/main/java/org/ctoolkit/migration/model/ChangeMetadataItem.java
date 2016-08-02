package org.ctoolkit.migration.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Change metadata item entity
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
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
