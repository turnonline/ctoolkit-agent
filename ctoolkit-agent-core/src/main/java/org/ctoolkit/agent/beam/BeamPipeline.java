package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Abstract class for beam pipeline
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class BeamPipeline<BATCH, OPTIONS extends PipelineOptions>
        implements Serializable
{
    /**
     * Create pipeline
     *
     * @param batch   link batch type  - {@link MigrationBatch} or {@link ImportBatch}
     * @param options pipeline options - {@link MigrationPipelineOptions} or {@link ImportPipelineOptions}
     * @return @link Pipeline
     */
    public abstract Pipeline create( BATCH batch, OPTIONS options );

    protected Logger log()
    {
        return LoggerFactory.getLogger( getClass() );
    }
}
