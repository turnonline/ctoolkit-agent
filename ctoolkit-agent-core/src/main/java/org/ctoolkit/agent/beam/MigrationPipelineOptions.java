package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;
import org.ctoolkit.agent.model.Agent;

/**
 * Migration pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface MigrationPipelineOptions
        extends PipelineOptions, JdbcPipelineOptions, ElasticsearchPipelineOptions
{
    @Description( "Flag if migration should by executed in 'dry run' mode (import to target agent will not be performed and" +
            "instead will be written to console). By default value is set to 'false', which means migrated data will be send" +
            "to target agent to perform import." )
    @Default.Boolean( false )
    boolean isDryRun();
    void setDryRun( boolean dryRun );

    @Validation.Required
    @Description( "Target agent url (for instance http://localhost:8080/api/v1/" )
    String getTargetAgentUrl();
    void setTargetAgentUrl( String targetAgentUrl );

    @Validation.Required
    @Description( "Target agent type." )
    Agent getTargetAgent();
    void setTargetAgent( Agent targetAgent );

    @Validation.Required
    @Description( "Number of rows per split. How many rows should be contained in one query split." )
    @Default.Integer(100)
    int getRowsPerSplit();
    void setRowsPerSplit( int rowsPerSplit );
}
