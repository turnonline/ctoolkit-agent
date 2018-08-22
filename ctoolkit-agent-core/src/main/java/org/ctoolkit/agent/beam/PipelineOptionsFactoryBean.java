package org.ctoolkit.agent.beam;

import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.PipelineOption;

import javax.inject.Singleton;
import java.util.List;

/**
 * Implementation of {@link PipelineOptionsFactory}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class PipelineOptionsFactoryBean
        implements PipelineOptionsFactory
{
    @Override
    public ImportPipelineOptions createImportPipelineOptions( ImportBatch batch )
    {
        ImportPipelineOptions options = org.apache.beam.sdk.options.PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( ImportPipelineOptions.class );

        setupJdbcPipelineOptions( options );
        setupElasticsearchPipelineOptions( options );

        return options;
    }

    @Override
    public MigrationPipelineOptions createMigrationPipelineOptions( MigrationBatch batch )
    {
        MigrationPipelineOptions options = org.apache.beam.sdk.options.PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( MigrationPipelineOptions.class );

        setupMigrationPipelineOptions( options );
        setupJdbcPipelineOptions( options );
        setupElasticsearchPipelineOptions( options );

        return options;
    }

    // -- private helpers

    private void setupMigrationPipelineOptions( MigrationPipelineOptions options )
    {
        String migrationTargetAgent = System.getProperty( "migrationTargetAgent" );
        String migrationTargetAgentUrl = System.getProperty( "migrationTargetAgentUrl" );

        if ( options.getTargetAgent() == null )
        {
            options.setTargetAgent( migrationTargetAgent != null ? Agent.valueOf( migrationTargetAgent ) : null);
        }

        if ( options.getTargetAgentUrl() == null )
        {
            options.setTargetAgentUrl( migrationTargetAgentUrl );
        }
    }

    private void setupJdbcPipelineOptions( JdbcPipelineOptions options )
    {
        String jdbcUrl = System.getProperty( "jdbcUrl" );
        String jdbcUsername = System.getProperty( "jdbcUsername" );
        String jdbcPassword = System.getProperty( "jdbcPassword" );
        String jdbcDriver = System.getProperty( "jdbcDriver" );

        if ( options.getJdbcUrl() == null )
        {
            options.setJdbcUrl( jdbcUrl );
        }
        if ( options.getJdbcUsername() == null )
        {
            options.setJdbcUsername( jdbcUsername );
        }
        if ( options.getJdbcPassword() == null )
        {
            options.setJdbcPassword( jdbcPassword );
        }
        if ( options.getJdbcDriver() == null )
        {
            options.setJdbcDriver( jdbcDriver );
        }
    }

    private void setupElasticsearchPipelineOptions( ElasticsearchPipelineOptions options )
    {
        String elasticHosts = System.getProperty( "elasticsearchHosts" );

        if ( options.getElasticsearchHosts() == null )
        {
            options.setElasticsearchHosts( elasticHosts != null ? elasticHosts.split( "," ) : null );
        }
    }

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
