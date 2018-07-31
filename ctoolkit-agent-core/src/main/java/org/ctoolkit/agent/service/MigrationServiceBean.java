package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.ImportBatch;
import org.ctoolkit.agent.model.ImportJob;
import org.ctoolkit.agent.model.MigrationBatch;
import org.ctoolkit.agent.model.MigrationJob;

import javax.inject.Singleton;

/**
 * Implementation of {@link MigrationService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationServiceBean
        implements MigrationService
{
    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        // TODO: implement
        MigrationJob job = new MigrationJob();
        job.setId( "1" );
        return job;
    }

    @Override
    public ImportJob importBatch( ImportBatch batch )
    {
        // TODO: implement
        ImportJob job = new ImportJob();
        job.setId( "1" );
        return job;
    }
}
