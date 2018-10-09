package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

/**
 * Mongo pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface MongoPipelineOptions
        extends PipelineOptions
{
    @Validation.Required
    @Description( "Mongo client URI" )
    String getMongoUri();
    void setMongoUri( String uri );
}
