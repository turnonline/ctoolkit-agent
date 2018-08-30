package org.ctoolkit.agent.service;

import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.beam.PipelineOptionsFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of {@link PipelineFacade}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class PipelineFacadeBean
        implements PipelineFacade
{
    @Inject
    private PipelineOptionsFactory pipelineOptionsFactory;

    @Inject
    private MigrationBeamPipeline migrationPipeline;

    @Inject
    private ImportBeamPipeline importPipeline;

    @Inject
    @Nullable
    private MigrationPipelineOptions migrationPipelineOptions;

    @Override
    public PipelineOptionsFactory pipelineOptionsFactory()
    {
        return pipelineOptionsFactory;
    }

    @Override
    public MigrationBeamPipeline migrationPipeline()
    {
        return migrationPipeline;
    }

    @Override
    public ImportBeamPipeline importPipeline()
    {
        return importPipeline;
    }

    @Override
    public MigrationPipelineOptions migrationPipelineOptions()
    {
        return migrationPipelineOptions;
    }
}
