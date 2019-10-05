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

import com.google.gson.Gson;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.DefaultHttpClient;
import io.micronaut.http.client.RxHttpClient;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.MigrationContext;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.service.beam.options.MigrationPipelineOptions;
import org.ctoolkit.agent.service.converter.BaseConverterRegistrat;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.rule.RuleSetResolver;
import org.ctoolkit.agent.service.transformer.TransformerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of {@link PipelineService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class PipelineServiceBean
        implements PipelineService
{
    private static final Logger log = LoggerFactory.getLogger( PipelineServiceBean.class );

    @Inject
    private PipelineFactory pipelineFactory;

    @Inject
    private Map<Agent, BaseConverterRegistrat> registrats;

    @Inject
    private RuleSetResolver ruleSetResolver;

    @Inject
    private TransformerExecutor transformerExecutor;

    @Inject
    @Nullable
    private MigrationPipelineOptions migrationPipelineOptions;

    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        Pipeline pipeline = pipelineFactory.createMigrationPipeline( batch );
        PipelineResult result = pipeline.run();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit( ( Runnable ) result::waitUntilFinish );
        executorService.shutdown();

        MigrationJob job = new MigrationJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public ImportJob importBatch( ImportBatch batch )
    {
        Pipeline pipeline = pipelineFactory.createImportPipeline( batch );
        PipelineResult result = pipeline.run();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit( ( Runnable ) result::waitUntilFinish );
        executorService.shutdown();

        ImportJob job = new ImportJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public List<ImportSet> transform( MigrationSet migrationSet, List<MigrationContext> migrationContextList )
    {
        if ( migrationPipelineOptions == null)
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via ApplicationContextFactory to hide this error." );
        }

        BaseConverterRegistrat registrat = registrats.get( migrationPipelineOptions.getTargetAgent() );

        // TODO: move to list bellow
        ConverterExecutor converterExecutor = new ConverterExecutor( transformerExecutor, registrat );
        converterExecutor.putToContext( MigrationSet.class, migrationSet );

        List<ImportSet> importSets = new ArrayList<>();

        for ( MigrationContext migrationContext : migrationContextList )
        {
            // TODO: remove putToContext from ConverterExecutor
            // TODO: put MigrationSet to context
            // TODO: put source to context
            // TODO: put target to context
            // TODO: put source to context

            // skip entity migration if rules return apply = 'false'
            if ( !ruleSetResolver.apply( migrationSet.getRuleSet(), migrationContext ) )
            {
                continue;
            }

            // set header values
            ImportSet importSet = new ImportSet();
            importSets.add( importSet );

            importSet.setAuthor( migrationSet.getAuthor() );
            importSet.setComment( "Migration import of " + migrationSet.getTarget().getNamespace() + "." + migrationSet.getTarget().getKind() );
            importSet.setNamespace( migrationSet.getTarget().getNamespace() );
            importSet.setKind( migrationSet.getTarget().getKind() );
            importSet.setId( converterExecutor.convertId( migrationSet, migrationContext ) );

            // migrate properties
//            for ( MigrationSetProperty migrationSetProperty : migrationSet.getProperties() )
//            {
//                MigrationContext.Property source = migrationContext.getProperties().get( migrationSetProperty.getSourceProperty() );
//                if ( source != null )
//                {
//                    // convert value to ImportSetProperty
//                    ImportSetProperty importSetProperty = converterExecutor.convertProperty( source.getValue(), migrationSetProperty );
//                    if ( importSetProperty != null )
//                    {
//                        importSet.getProperties().add( importSetProperty );
//                    }
//                }
//            }
        }

        return importSets;
    }

    @Override
    public void importToTargetAgent( List<ImportSet> importSets )
    {
        if ( migrationPipelineOptions == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via ApplicationContextFactory to hide this error." );
        }

        ImportBatch importBatch = new ImportBatch();
        importBatch.setImportSets( importSets );

        if ( migrationPipelineOptions.isDryRun() )
        {
            log.info( new Gson().toJson( importBatch ) );
        }
        else
        {
            try
            {
                RxHttpClient httpClient = new DefaultHttpClient( new URL( migrationPipelineOptions.getTargetAgentUrl() ) );
                MutableHttpRequest<ImportBatch> post = HttpRequest.POST( new URI( "/api/v1/imports" ), importBatch );
                httpClient.retrieve( post ).blockingFirst();
            }
            catch ( MalformedURLException | URISyntaxException e )
            {
                log.error( "Unable to construct migration client", e );
            }
        }
    }
}
