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
    private String mapReduceMigrationJobId;

    @Override
    protected ExportMetadataItem newItem()
    {
        return new ExportMetadataItem( this );
    }

    public String getMapReduceMigrationJobId()
    {
        return mapReduceMigrationJobId;
    }

    public void setMapReduceMigrationJobId( String mapReduceMigrationJobId )
    {
        this.mapReduceMigrationJobId = mapReduceMigrationJobId;
    }

    @Override
    public String toString()
    {
        return "ExportMetadata{" +
                "mapReduceMigrationJobId='" + mapReduceMigrationJobId + '\'' +
                "} " + super.toString();
    }
}
