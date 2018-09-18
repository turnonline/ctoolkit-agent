package org.ctoolkit.agent.service;

import com.google.gson.Gson;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.DefaultHttpClient;
import io.micronaut.http.client.RxHttpClient;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.converter.BaseConverterRegistrat;
import org.ctoolkit.agent.converter.ConverterExecutor;
import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.rule.RuleSetResolver;
import org.ctoolkit.agent.transformer.TransformerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Implementation of {@link MigrationService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class MigrationServiceBean
        implements MigrationService
{
    private static final Logger log = LoggerFactory.getLogger( MigrationServiceBean.class );

    @Inject
    private PipelineFacade pipelineFacade;

    @Inject
    private Map<Agent, BaseConverterRegistrat> registrats;

    @Inject
    private RuleSetResolver ruleSetResolver;

    @Inject
    private TransformerExecutor transformerExecutor;

    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        MigrationPipelineOptions options = pipelineFacade.pipelineOptionsFactory().createMigrationPipelineOptions( batch );
        Pipeline pipeline = pipelineFacade.migrationPipeline().create( batch, options );
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
        ImportPipelineOptions options = pipelineFacade.pipelineOptionsFactory().createImportPipelineOptions( batch );
        Pipeline pipeline = pipelineFacade.importPipeline().create( batch, options );
        PipelineResult result = pipeline.run();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit( ( Runnable ) result::waitUntilFinish );
        executorService.shutdown();

        ImportJob job = new ImportJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public List<ImportSet> transform( MigrationSet migrationSet, List<EntityExportData> entityExportDataList )
    {
        if ( pipelineFacade.migrationPipelineOptions() == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via factory to hide this error." );
        }

        BaseConverterRegistrat registrat = registrats.get( pipelineFacade.migrationPipelineOptions().getTargetAgent() );
        ConverterExecutor converterExecutor = new ConverterExecutor( transformerExecutor, registrat );
        converterExecutor.putToContext( MigrationSet.class, migrationSet );

        List<ImportSet> importSets = new ArrayList<>();

        for ( EntityExportData entityExportData : entityExportDataList )
        {
            // skip entity migration if rules return apply = 'false'
            if ( !ruleSetResolver.apply( migrationSet.getRuleSet(), entityExportData ) )
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
            importSet.setId( converterExecutor.convertId( migrationSet, entityExportData ) );

            // migrate properties
            for ( MigrationSetProperty migrationSetProperty : migrationSet.getProperties() )
            {
                EntityExportData.Property source = entityExportData.getProperties().get( migrationSetProperty.getSourceProperty() );
                if ( source != null )
                {
                    // convert value to ImportSetProperty
                    ImportSetProperty importSetProperty = converterExecutor.convertProperty( source.getValue(), migrationSetProperty );
                    if ( importSetProperty != null )
                    {
                        importSet.getProperties().add( importSetProperty );
                    }
                }
            }
        }

        return importSets;
    }

    @Override
    public void importToTargetAgent( List<ImportSet> importSets )
    {
        MigrationPipelineOptions options = pipelineFacade.migrationPipelineOptions();
        if ( options == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via factory to hide this error." );
        }

        ImportBatch importBatch = new ImportBatch();
        importBatch.setImportSets( importSets );

        if ( options.isDryRun() )
        {
            log.info( new Gson().toJson( importBatch ) );
        }
        else
        {
            try
            {
                RxHttpClient httpClient = new DefaultHttpClient( new URL( options.getTargetAgentUrl() ) );
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
