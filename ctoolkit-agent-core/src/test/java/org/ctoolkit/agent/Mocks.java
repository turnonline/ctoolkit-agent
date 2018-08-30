package org.ctoolkit.agent;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.model.api.MigrationSetTarget;

/**
 * Mocks for unit tests
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Mocks
{
    public static EntityExportData exportData( String name, Object value )
    {
        EntityExportData exportData = new EntityExportData();
        exportData.getProperties().put( name, new EntityExportData.Property( value ) );

        return exportData;
    }

    public static MigrationSetSource migrationSetSource(String namespace, String kind)
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setNamespace( namespace );
        source.setKind( kind );

        return source;
    }

    public static MigrationSetTarget migrationSetTarget( String namespace, String kind)
    {
        MigrationSetTarget target = new MigrationSetTarget();
        target.setNamespace( namespace );
        target.setKind( kind );

        return target;
    }

    public static MigrationSetProperty mockMigrationSetPropery( String type )
    {
        MigrationSetProperty property = new MigrationSetProperty();
        property.setTargetType( type );
        property.setTargetProperty( "name" );
        return property;
    }
}
