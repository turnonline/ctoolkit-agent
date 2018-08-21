package org.ctoolkit.agent.service;

import com.google.gson.Gson;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.DefaultHttpClient;
import io.micronaut.http.client.RxHttpClient;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.beam.PipelineOptionsFactory;
import org.ctoolkit.agent.converter.ConverterRegistrat;
import org.ctoolkit.agent.model.Agent;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.ValueWithLabels;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.rule.RuleSetResolver;
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
    private PipelineOptionsFactory pipelineOptionsFactory;

    @Inject
    private MigrationBeamPipeline migrationPipeline;

    @Inject
    private ImportBeamPipeline importPipeline;

    @Inject
    @Nullable
    private MigrationPipelineOptions migrationPipelineOptions;

    @Inject
    private Map<Agent, ConverterRegistrat> registrats;

    @Inject
    private RuleSetResolver ruleSetResolver;

    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        MigrationPipelineOptions options = pipelineOptionsFactory.createMigrationPipelineOptions( batch );
        Pipeline pipeline = migrationPipeline.create( batch, options );
        PipelineResult result = pipeline.run();

        MigrationJob job = new MigrationJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public ImportJob importBatch( ImportBatch batch )
    {
        ImportPipelineOptions options = pipelineOptionsFactory.createImportPipelineOptions( batch );
        Pipeline pipeline = importPipeline.create( batch, options );
        PipelineResult result = pipeline.run();

        ImportJob job = new ImportJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public List<ImportSet> transform( MigrationSet migrationSet, List<EntityExportData> entityExportDataList )
    {
        if ( migrationPipelineOptions == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via factory to hide this error." );
        }

        ConverterRegistrat registrat = registrats.get( migrationPipelineOptions.getTargetAgent() );
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
            importSet.setComment( "Migration import of " + migrationSet.getTargetNamespace() + "." + migrationSet.getTargetKind() );
            importSet.setClean( migrationSet.getClean() );
            importSet.setNamespace( migrationSet.getTargetNamespace() );
            importSet.setKind( migrationSet.getTargetKind() );

            // retrieve parent
            if ( migrationSet.getTargetParentKind() != null )
            {
                EntityExportData.Property property = entityExportData.getProperties().get( migrationSet.getSourceParentForeignIdPropertyName() );
                importSet.setParentNamespace( migrationSet.getTargetNamespace() );
                importSet.setParentKind( migrationSet.getTargetParentKind() );
                importSet.setParentId( new ValueWithLabels( property.getValue() )
                        .addLabel( "name", migrationSet.getTargetParentId() )
                        .addLabel( "lookup", migrationSet.getTargetParentLookupPropertyName() )
                        .toString() );
            }

            // migrate properties
            for ( MigrationSetProperty migrationSetProperty : migrationSet.getProperties() )
            {
                EntityExportData.Property source = entityExportData.getProperties().get( migrationSetProperty.getSourceProperty() );
                if ( source != null )
                {
                    // convert value to ImportSetProperty
                    ImportSetProperty importSetProperty = registrat.convert( source.getValue(), migrationSetProperty );
                    if ( importSetProperty != null )
                    {
                        importSet.getProperties().add( importSetProperty );

                        // retrieve sync id property
                        EntityExportData.Property syncIdProperty = entityExportData.getProperties().get( migrationSet.getSourceSyncIdPropertyName() );
                        if ( syncIdProperty != null )
                        {
                            importSet.setSyncId( new ValueWithLabels( syncIdProperty.getValue() )
                                    .addLabel( "name", migrationSet.getTargetSyncIdPropertyName() )
                                    .toString() );
                        }
                    }
                }
            }
        }

        return importSets;
    }

    @Override
    public void importToTargetAgent( List<ImportSet> importSets )
    {
        if ( migrationPipelineOptions == null )
        {
            throw new NullPointerException( "Migration pipeline options cannot be null. Provide MigrationPipelineOptions via factory to hide this error." );
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
