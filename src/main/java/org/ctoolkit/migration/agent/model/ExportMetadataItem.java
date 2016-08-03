package org.ctoolkit.migration.agent.model;

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
    private String entityToExport;

    public ExportMetadataItem()
    {
    }

    public ExportMetadataItem( ExportMetadata metadata )
    {
        super( metadata );
    }

    public String getEntityToExport()
    {
        return entityToExport;
    }

    public void setEntityToExport( String entityToExport )
    {
        this.entityToExport = entityToExport;
    }

    @Override
    public String toString()
    {
        return "ExportMetadataItem{" +
                "entityToExport='" + entityToExport + '\'' +
                "} " + super.toString();
    }
}
