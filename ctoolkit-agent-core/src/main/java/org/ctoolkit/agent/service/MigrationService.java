package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.util.List;

/**
 * Public migration service API
 *
 * @author <a href="mailto:pohorelec@turnonline.biz">Jozef Pohorelec</a>
 */
public interface MigrationService
{
    /**
     * Migrate batch of migration sets
     *
     * @param batch {@link MigrationBatch}
     * @return {@link MigrationJob}
     */
    MigrationJob migrateBatch( MigrationBatch batch );

    /**
     * Import batch of import sets
     *
     * @param batch {@link ImportBatch}
     * @return {@link ImportJob}
     */
    ImportJob importBatch( ImportBatch batch );

    /**
     * Transform list of entities to list of {@link ImportSet} using {@link MigrationSet} rules
     *
     * @param migrationSet       {@link MigrationSet}
     * @param entityExportDataList list of {@link EntityExportData}
     * @return {@link ImportSet}
     */
    List<ImportSet> transform( MigrationSet migrationSet, List<EntityExportData> entityExportDataList );

    /**
     * Import list {@link ImportSet} to target agent
     *
     * @param importSets {@link ImportSet}
     */
    void importToTargetAgent( List<ImportSet> importSets );
}
