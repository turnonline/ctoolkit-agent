/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.service.beam.pipeline;

import com.google.common.annotations.VisibleForTesting;
import org.apache.beam.sdk.Pipeline;
import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.PipelineOption;
import org.ctoolkit.agent.service.beam.options.ElasticsearchPipelineOptions;
import org.ctoolkit.agent.service.beam.options.ImportPipelineOptions;
import org.ctoolkit.agent.service.beam.options.JdbcPipelineOptions;
import org.ctoolkit.agent.service.beam.options.MigrationPipelineOptions;
import org.ctoolkit.agent.service.beam.options.MongoPipelineOptions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Implementation of {@link PipelineFactory}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class PipelineFactoryBean
        implements PipelineFactory
{
    @Inject
    private ImportBeamPipeline importBeamPipeline;

    @Inject
    private MigrationBeamPipeline migrationBeamPipeline;

    @Override
    public Pipeline createImportPipeline( ImportBatch batch )
    {
        ImportPipelineOptions options = createImportPipelineOptions( batch );
        return importBeamPipeline.create( batch, options );
    }

    @Override
    public Pipeline createMigrationPipeline( MigrationBatch batch )
    {
        MigrationPipelineOptions options = createMigrationPipelineOptions( batch );
        return migrationBeamPipeline.create( batch, options );
    }

    // -- private helpers

    @VisibleForTesting
    protected ImportPipelineOptions createImportPipelineOptions( ImportBatch batch )
    {
        ImportPipelineOptions options = org.apache.beam.sdk.options.PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( ImportPipelineOptions.class );

        setupJdbcPipelineOptions( options );
        setupElasticsearchPipelineOptions( options );
        setupMongoPipelineOptions( options );

        options.setAppName( "Data import" );

        return options;
    }

    @VisibleForTesting
    protected MigrationPipelineOptions createMigrationPipelineOptions( MigrationBatch batch )
    {
        MigrationPipelineOptions options = org.apache.beam.sdk.options.PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( MigrationPipelineOptions.class );

        setupMigrationPipelineOptions( options );
        setupJdbcPipelineOptions( options );
        setupElasticsearchPipelineOptions( options );
        setupMongoPipelineOptions( options );

        options.setAppName( "Data migration" );

        return options;
    }

    private void setupMigrationPipelineOptions( MigrationPipelineOptions options )
    {
        String migrationTargetAgent = System.getProperty( "migrationTargetAgent" );
        String migrationTargetAgentUrl = System.getProperty( "migrationTargetAgentUrl" );

        if ( options.getTargetAgent() == null )
        {
            options.setTargetAgent( migrationTargetAgent != null ? Agent.valueOf( migrationTargetAgent ) : null );
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

    private void setupMongoPipelineOptions( MongoPipelineOptions options )
    {
        String mongoUri = System.getProperty( "mongoUri" );

        if ( options.getMongoUri() == null )
        {
            options.setMongoUri( mongoUri );
        }
    }

    private String[] toArgs( List<PipelineOption> options )
    {
        // setup runner
        boolean containsRunner = options.stream().anyMatch( pipelineOption -> "runner".equals( pipelineOption.getName() ) );
        String runner = System.getProperty( "runner" );
        if ( !containsRunner && runner != null )
        {
            PipelineOption pipelineOption = new PipelineOption();
            pipelineOption.setName( "runner" );
            pipelineOption.setValue( runner );
            options.add( pipelineOption );
        }

        String[] args = new String[options.size()];
        for ( int i = 0; i < options.size(); i++ )
        {
            PipelineOption option = options.get( i );
            args[i] = "--" + option.getName() + "=" + option.getValue();
        }

        return args;
    }
}
