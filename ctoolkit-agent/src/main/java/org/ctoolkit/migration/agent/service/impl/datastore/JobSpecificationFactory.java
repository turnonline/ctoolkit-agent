package org.ctoolkit.migration.agent.service.impl.datastore;

import org.ctoolkit.migration.agent.annotation.ChangeJob;
import org.ctoolkit.migration.agent.annotation.ExportJob;
import org.ctoolkit.migration.agent.annotation.ImportJob;
import org.ctoolkit.migration.agent.annotation.MigrateJob;
import org.ctoolkit.migration.agent.model.CtoolkitAgentConfiguration;
import org.ctoolkit.migration.agent.model.MigrationJobConfiguration;

/**
 * Job specification factory for map reduce
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface JobSpecificationFactory
{
    @ImportJob
    MapSpecificationProvider createImportJobSpecification( String parentKey );

    @ChangeJob
    MapSpecificationProvider createChangeJobSpecification( String parentKey );

    @ExportJob
    MapSpecificationProvider createExportJobSpecification( String parentKey );

    @MigrateJob
    MapSpecificationProvider createMigrateJobSpecification( MigrationJobConfiguration jobConfiguration, CtoolkitAgentConfiguration configuration );
}
