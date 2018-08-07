package org.ctoolkit.agent.service;

import com.google.gson.Gson;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.ctoolkit.agent.beam.ImportBeamPipeline;
import org.ctoolkit.agent.beam.ImportPipelineOptions;
import org.ctoolkit.agent.beam.JdbcPipelineOptions;
import org.ctoolkit.agent.beam.MigrationBeamPipeline;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
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
import org.ctoolkit.agent.model.api.PipelineOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
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
    private MigrationBeamPipeline migrationPipeline;

    @Inject
    private ImportBeamPipeline importPipeline;

    @Inject
    @Nullable
    private MigrationPipelineOptions migrationPipelineOptions;

    @Inject
    private Map<Agent, ConverterRegistrat> registrats;

    @Override
    public MigrationJob migrateBatch( MigrationBatch batch )
    {
        MigrationPipelineOptions options = PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( MigrationPipelineOptions.class );
        setupJdbcPipelineOptions( options );

        Pipeline pipeline = migrationPipeline.create( batch, options );
        PipelineResult result = pipeline.run();

        MigrationJob job = new MigrationJob();
        job.setState( result.getState().name() );
        return job;
    }

    @Override
    public ImportJob importBatch( ImportBatch batch )
    {
        ImportPipelineOptions options = PipelineOptionsFactory
                .fromArgs( toArgs( batch.getPipelineOptions() ) )
                .as( ImportPipelineOptions.class );
        setupJdbcPipelineOptions( options );

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
            ImportSet importSet = new ImportSet();
            importSets.add( importSet );

            importSet.setAuthor( migrationSet.getAuthor() );
            importSet.setComment( "Migration import of " + migrationSet.getTargetNamespace() + "." + migrationSet.getTargetKind() );
            importSet.setClean( migrationSet.getClean() );
            importSet.setNamespace( migrationSet.getTargetNamespace() );
            importSet.setKind( migrationSet.getTargetKind() );

            // TODO: implement rules

            // retrieve parent
            if (migrationSet.getTargetParentKind() != null)
            {
                EntityExportData.Property property = entityExportData.getProperties().get( migrationSet.getSourceParentForeignIdPropertyName() );
                importSet.setParentNamespace( migrationSet.getTargetNamespace() );
                importSet.setParentKind( migrationSet.getTargetParentKind() );
                importSet.setParentId( new ValueWithLabels(property.getValue())
                        .addLabel( "name", migrationSet.getTargetParentId() )
                        .addLabel( "lookup", migrationSet.getTargetParentLookupPropertyName() )
                        .toString() );
            }

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
                            importSet.setSyncId( new ValueWithLabels( syncIdProperty.getValue())
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
            // TODO: implement (create ctoolkit-agent-client)
            // TODO: call ctoolkit-agent-client importBatch method
        }
    }

    // -- private helpers

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
