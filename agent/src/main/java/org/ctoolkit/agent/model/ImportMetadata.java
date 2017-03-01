package org.ctoolkit.agent.model;

import com.googlecode.objectify.annotation.Entity;

/**
 * Import metadata entity.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
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
