package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.EntityMetaData;
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
     * Transform list of entities to {@link ImportBatch} using {@link MigrationSet} rules
     *
     * @param migrationSet       {@link MigrationSet}
     * @param entityMetaDataList list of {@link EntityMetaData}
     * @return {@link ImportSet}
     */
    ImportBatch transform( MigrationSet migrationSet, List<EntityMetaData> entityMetaDataList );

    /**
     * Import {@link ImportBatch} to target agent
     *
     * @param batch          {@link ImportBatch}
     */
    void importToTargetAgent( ImportBatch batch );
}
