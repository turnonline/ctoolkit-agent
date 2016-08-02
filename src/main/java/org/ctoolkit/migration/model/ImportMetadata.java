package org.ctoolkit.migration.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Import metadata entity.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity( name = "_ImportMetadata" )
public class ImportMetadata
        extends BaseMetadata<ImportMetadataItem>
{
    @Override
    protected ImportMetadataItem newItem()
    {
        return new ImportMetadataItem( this );
    }

    @Override
    public String toString()
    {
        return "ImportMetadata{} " + super.toString();
    }
}
