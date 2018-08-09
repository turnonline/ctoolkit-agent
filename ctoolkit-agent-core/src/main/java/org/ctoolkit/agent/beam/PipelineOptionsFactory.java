package org.ctoolkit.agent.beam;

import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationBatch;

/**
 * Pipeline options factory
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface PipelineOptionsFactory
{
    /**
     * Create {@link ImportPipelineOptions}
     * @param batch {@link ImportBatch} contains pipeline options provided by client
     * @return {@link ImportPipelineOptions}
     */
    ImportPipelineOptions createImportPipelineOptions( ImportBatch batch );

    /**
     * Create {@link MigrationPipelineOptions}
     * @param batch {@link ImportBatch} contains pipeline options provided by client
     * @return {@link MigrationBatch}
     */
    MigrationPipelineOptions createMigrationPipelineOptions( MigrationBatch batch );
}
