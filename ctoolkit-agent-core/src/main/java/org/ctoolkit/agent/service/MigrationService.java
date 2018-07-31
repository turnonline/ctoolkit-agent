package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.ImportBatch;
import org.ctoolkit.agent.model.ImportJob;
import org.ctoolkit.agent.model.MigrationBatch;
import org.ctoolkit.agent.model.MigrationJob;

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
    MigrationJob migrateBatch( MigrationBatch batch);

    /**
     * Import batch of import sets
     *
     * @param batch {@link ImportBatch}
     * @return {@link ImportJob}
     */
    ImportJob importBatch( ImportBatch batch);
}
