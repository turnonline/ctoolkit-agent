package org.ctoolkit.migration.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Export metadata entity.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity( name = "_ExportMetadata" )
public class ExportMetadata
        extends BaseMetadata<ExportMetadataItem>
{
    @Override
    protected ExportMetadataItem newItem()
    {
        return new ExportMetadataItem( this );
    }

    @Override
    public String toString()
    {
        return "ExportMetadata{} " + super.toString();
    }
}
