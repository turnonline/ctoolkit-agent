package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.util.List;

/**
 * Worker service as a support for beams {@link org.apache.beam.sdk.transforms.DoFn} functions
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface WorkerService
{
    List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit );

    List<EntityMetaData> retrieveEntityMetaDataList( String sql );

    void migrate( MigrationSet migrationSet, List<EntityMetaData> entityMetaDataList );
}
