package org.ctoolkit.agent.service;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.PipelineOption;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Implementation of {@link MigrationService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationServiceBean
        implements MigrationService
{
    @Inject
    private MigrationBeamPipeline migrationPipeline;

    @Inject
    private ImportBeamPipeline importPipeline;

    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        MigrationPipelineOptions options = PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( MigrationPipelineOptions.class );

        Pipeline pipeline = migrationPipeline.create( options );
        PipelineResult result = pipeline.run();

        MigrationJob job = new MigrationJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public ImportJob importBatch( ImportBatch batch )
    {
        ImportPipelineOptions options = PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( ImportPipelineOptions.class );

        Pipeline pipeline = importPipeline.create( options );
        PipelineResult result = pipeline.run();

        ImportJob job = new ImportJob();
        job.setState( result.getState().name() );
        return job;
    }

    // -- private helpers

    private String[] toArgs( List<PipelineOption> options )
    {
        String[] args = new String[options.size()];
        for ( int i = 0; i < options.size(); i++ )
        {
            PipelineOption option = options.get( i );
            args[i] = "--" + option.getName() + "=" + option.getValue();
        }

        return args;
    }
}
