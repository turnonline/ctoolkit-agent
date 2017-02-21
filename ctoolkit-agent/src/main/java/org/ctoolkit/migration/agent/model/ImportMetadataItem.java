package org.ctoolkit.migration.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Import metadata item entity
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Entity( name = "_ImportMetadataItem" )
public class ImportMetadataItem
        extends BaseMetadataItem<ImportMetadata>
{
    public ImportMetadataItem()
    {
    }

    public ImportMetadataItem( ImportMetadata metadata )
    {
        super( metadata );
    }

    @Override
    public String toString()
    {
        return "ImportMetadataItem{} " + super.toString();
    }
}
