package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Implementation of {@link WorkerService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class WorkerServiceBean
        implements WorkerService
{
    private static final Logger log = LoggerFactory.getLogger( WorkerServiceBean.class );

    @Inject
    private RestHighLevelClient elasticClient;

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        // TODO: implement
        return null;
    }

    public List<EntityExportData> retrieveEntityMetaDataList( String sql )
    {
        // TODO: implement
        return null;
    }

    @Override
    public void importData( ImportSet importSet )
    {
        // TODO: implement
        log.info( importSet.toString() );
    }
}
