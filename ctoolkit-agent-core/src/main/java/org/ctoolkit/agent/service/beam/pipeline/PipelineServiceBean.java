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
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.Export;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.model.api.MigrationSetTarget;
import org.ctoolkit.agent.service.beam.options.MigrationPipelineOptions;
import org.ctoolkit.agent.service.connector.ConnectorFacade;
import org.ctoolkit.agent.service.converter.BaseConverterRegistrat;
import org.ctoolkit.agent.service.converter.ConverterExecutor;
import org.ctoolkit.agent.service.enricher.EnricherExecutor;
import org.ctoolkit.agent.service.rule.RuleSetResolver;
import org.ctoolkit.agent.service.transformer.TransformerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
    private ConnectorFacade connectorFacade;

    @Inject
    private PipelineFactory pipelineFactory;

    @Inject
    private Map<Agent, BaseConverterRegistrat> registrats;

    @Inject
    private RuleSetResolver ruleSetResolver;

    @Inject
    private TransformerExecutor transformerExecutor;

    @Inject
    private EnricherExecutor enricherExecutor;

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
    public List<ImportSet> transform( MigrationSet migrationSet, List<Export> exports )
    {
        if ( migrationPipelineOptions == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Call ApplicationContextFactory.create() in your DoFn class to hide this error." );
        }

        BaseConverterRegistrat registrat = registrats.get( migrationPipelineOptions.getTargetAgent() );
        List<ImportSet> importSets = new ArrayList<>();

        for ( Export export : exports )
        {
            Map<String, Object> ctx = new HashMap<>( export );

            // skip entity migration if rules return apply = 'false'
            if ( !ruleSetResolver.apply( migrationSet.getRuleSet(), ctx ) )
            {
                continue;
            }

            ConverterExecutor converterExecutor = new ConverterExecutor( enricherExecutor, transformerExecutor, registrat );
            converterExecutor.putToContext( migrationSet );
            converterExecutor.getCtx().putAll( ctx );

            // enrich migration context
            converterExecutor.enrich( ctx, migrationSet.getEnricherGroups() );

            // set header values
            MigrationSetTarget target = migrationSet.getTarget();
            MigrationSetSource source = migrationSet.getSource();

            ImportSet importSet = new ImportSet();
            importSets.add( importSet );
            importSet.setAuthor( migrationSet.getAuthor() );
            importSet.setComment( "Migration import of " + target.getNamespace() + "." + target.getKind() );
            importSet.setNamespace( target.getNamespace() );
            importSet.setKind( target.getKind() );

            importSet.setClean( migrationSet.getTarget().getClean() );
            importSet.setId( converterExecutor.convertId( migrationSet, ctx ) );
            importSet.setChangeDate( source.getChangeDate() );
            importSet.setIdSelector( target.getIdSelector() );
            importSet.setSyncDateProperty( target.getSyncDateProperty() );

            // migrate properties
            for ( MigrationSetProperty migrationSetProperty : migrationSet.getProperties() )
            {
                addImportSetProperty( ( currentMigrationSetProperty, index ) -> {
                            String sourcePropertyNameRaw = currentMigrationSetProperty.getSourceProperty();
                            String sourcePropertyName = sourcePropertyNameRaw != null ? sourcePropertyNameRaw.replaceAll( "\\*", String.valueOf( index ) ) : null;

                            Object sourceProperty = ctx.get( sourcePropertyName );
                            Object targetProperty = currentMigrationSetProperty.getTargetValue();

                            if ( sourceProperty == null && targetProperty == null )
                            {
                                return null;
                            }

                            // put source and target object into converter context
                            converterExecutor.getCtx().put( "source.value", sourceProperty );
                            converterExecutor.getCtx().put( "target.value", targetProperty );

                            // convert property
                            return converterExecutor.convertProperty( sourceProperty, currentMigrationSetProperty );
                        },
                        Collections.singletonList( migrationSetProperty ),
                        importSet.getProperties(),
                        0 );

            }
        }

        return importSets;
    }

    @Override
    public void importToTargetAgent( List<ImportSet> importSets )
    {
        if ( migrationPipelineOptions == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Call ApplicationContextFactory.create() in your DoFn class to hide this error." );
        }

        ImportBatch importBatch = new ImportBatch();
        importBatch.setImportSets( importSets );

        if ( migrationPipelineOptions.isDryRun() )
        {
            log.info( new Gson().toJson( importBatch ) );
        }
        else
        {
            String baseUrl = migrationPipelineOptions.getTargetAgentUrl();
            String importsPath = "/api/v1/imports";

            connectorFacade.push( baseUrl + importsPath, importBatch );
        }
    }

    // -- private helpers

    @SuppressWarnings( "unchecked" )
    private Object addImportSetProperty( ImportSetPropertySupplier importSetPropertySupplier,
                                         List<MigrationSetProperty> migrationSetProperties,
                                         List<ImportSetProperty> importSetProperties,
                                         int index )
    {
        for ( MigrationSetProperty migrationSetProperty : migrationSetProperties )
        {
            String targetType = migrationSetProperty.getTargetType();

            switch ( targetType )
            {
                case "list":
                {
                    ImportSetProperty importSetProperty = new ImportSetProperty();
                    importSetProperty.setName( migrationSetProperty.getTargetProperty() );
                    importSetProperty.setType( migrationSetProperty.getTargetType() );
                    importSetProperty.setValue( new ArrayList<>() );

                    initializeImportSetProperties(
                            importSetProperties,
                            migrationSetProperty,
                            importSetProperty
                    );

                    int previousSize = 0;
                    int currentSize = 0;

                    while ( previousSize != currentSize || currentSize == 0 )
                    {
                        List<ImportSetProperty> currentListProperties = ( List<ImportSetProperty> ) importSetProperty.getValue();
                        previousSize = currentListProperties.size();

                        addImportSetProperty(
                                importSetPropertySupplier,
                                ( List<MigrationSetProperty> ) migrationSetProperty.getTargetValue(),
                                currentListProperties,
                                index
                        );

                        currentSize = currentListProperties.size();

                        index++;
                    }

                    break;
                }
                case "object":
                {
                    ImportSetProperty importSetProperty = new ImportSetProperty();
                    importSetProperty.setName( migrationSetProperty.getTargetProperty() );
                    importSetProperty.setType( migrationSetProperty.getTargetType() );

                    List<ImportSetProperty> currentObjectProperties = initializeImportSetProperties(
                            importSetProperties,
                            migrationSetProperty,
                            importSetProperty
                    );

                    importSetProperty.setValue( addImportSetProperty(
                            importSetPropertySupplier,
                            ( List<MigrationSetProperty> ) migrationSetProperty.getTargetValue(),
                            currentObjectProperties,
                            0
                    ) );

                    break;
                }
                default:
                {
                    ImportSetProperty importSetProperty = importSetPropertySupplier.get( migrationSetProperty, index );
                    if ( importSetProperty != null )
                    {
                        importSetProperties.add( importSetProperty );
                    }
                }
            }
        }

        return importSetProperties;
    }

    private List<ImportSetProperty> initializeImportSetProperties( List<ImportSetProperty> importSetProperties,
                                                                   MigrationSetProperty migrationSetProperty,
                                                                   ImportSetProperty importSetProperty )
    {
        List<ImportSetProperty> existingImportSetProperties = importSetProperties
                .stream()
                .filter( isp -> isp.getName().equals( migrationSetProperty.getTargetProperty() ) )
                .collect( Collectors.toList() );

        if ( existingImportSetProperties.isEmpty() )
        {
            importSetProperties.add( importSetProperty );
        }

        return existingImportSetProperties;
    }
}
