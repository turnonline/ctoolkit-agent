package org.ctoolkit.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Export metadata item entity
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity( name = "_ExportMetadataItem" )
public class ExportMetadataItem
        extends BaseMetadataItem<ExportMetadata>
{
    public ExportMetadataItem()
    {
    }

    public ExportMetadataItem( ExportMetadata metadata )
    {
        super( metadata );
    }

    @Override
    public String toString()
    {
        return "ExportMetadataItem{} " + super.toString();
    }
}
