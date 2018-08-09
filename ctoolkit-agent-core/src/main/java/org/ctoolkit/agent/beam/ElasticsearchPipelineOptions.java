package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

/**
 * Elasticsearch pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ElasticsearchPipelineOptions
        extends PipelineOptions
{
    @Validation.Required
    @Description( "Elasticsearch hosts" )
    String[] getElasticsearchHosts();
    void setElasticsearchHosts( String[] hosts );
}
