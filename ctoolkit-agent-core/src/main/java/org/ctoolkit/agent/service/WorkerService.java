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
    /**
     * Split root query to offset queries.
     *
     * @param migrationSet used to retrieve root query
     * @param rowsPerSplit number of rows per one query split
     * @return list of split queries
     */
    List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit );

    /**
     * Retrieve list of {@link EntityMetaData} for specified sql query
     * @param sql query
     * @return list of {@link EntityMetaData}
     */
    List<EntityMetaData> retrieveEntityMetaDataList( String sql );
}
