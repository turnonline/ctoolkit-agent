package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.options.ApplicationNameOptions;
import org.apache.beam.sdk.options.PipelineOptions;

/**
 * Import pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ImportPipelineOptions
        extends PipelineOptions, ApplicationNameOptions, JdbcPipelineOptions, ElasticsearchPipelineOptions, MongoPipelineOptions
{
}
