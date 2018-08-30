package org.ctoolkit.agent.service;

import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.beam.PipelineOptionsFactory;

/**
 * Pipeline facade API
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface PipelineFacade
{
    PipelineOptionsFactory pipelineOptionsFactory();

    MigrationBeamPipeline migrationPipeline();

    ImportBeamPipeline importPipeline();

    MigrationPipelineOptions migrationPipelineOptions();
}
