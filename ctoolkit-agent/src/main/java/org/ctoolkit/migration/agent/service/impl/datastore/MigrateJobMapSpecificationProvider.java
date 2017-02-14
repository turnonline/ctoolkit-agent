package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.OutputWriter;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.NoOutput;
import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.api.migration.CtoolkitAgent;
import org.ctoolkit.api.migration.model.ImportBatch;
import org.ctoolkit.migration.agent.config.CtoolkitAgentFactory;
import org.ctoolkit.migration.agent.model.CtoolkitAgentConfiguration;
import org.ctoolkit.migration.agent.model.MigrationJobConfiguration;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import static org.ctoolkit.migration.agent.service.impl.datastore.BatchMapOnlyMapperJob.injector;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrateJobMapSpecificationProvider
        implements MapSpecificationProvider, Serializable
{
    private static final long serialVersionUID = 8477680668820034478L;

    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private final MigrationJobConfiguration jobConfiguration;

    private final CtoolkitAgentConfiguration configuration;

    private final MigrateMapOnlyMapperJob mapper;

    @Inject
    public MigrateJobMapSpecificationProvider( @Assisted MigrationJobConfiguration jobConfiguration,
                                               @Assisted CtoolkitAgentConfiguration configuration,
                                               MigrateMapOnlyMapperJob mapper )
    {
        this.jobConfiguration = jobConfiguration;
        this.configuration = configuration;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MapSpecification<Entity, Entity, Entity> get()
    {
        Query query = new Query( "_ExportMetadataItem" ).setAncestor( KeyFactory.stringToKey( jobConfiguration.getExportId() ) );
        DatastoreInput input = new DatastoreInput( query, SHARD_COUNT );

        return new MapSpecification.Builder<>( input, mapper, new NoOutput<Entity, Entity>()
        {
            @Override
            public Entity finish( Collection<? extends OutputWriter<Entity>> outputWriters )
            {
                // start job to import data
                CtoolkitAgent ctoolkitAgent = injector.getInstance( CtoolkitAgentFactory.class )
                        .provideCtoolkitAgent( configuration ).get();

                try
                {
                    ctoolkitAgent.importBatch().job().start( jobConfiguration.getImportId(), new ImportBatch() ).execute();
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( "Unable to start import job", e );
                }

                return null;
            }
        } ).setJobName( "MigrationJob" ).build();
    }
}
