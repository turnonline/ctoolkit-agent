package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.OutputWriter;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.NoOutput;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.agent.model.MigrationJobConfiguration;
import org.ctoolkit.restapi.client.Identifier;
import org.ctoolkit.restapi.client.RequestCredential;
import org.ctoolkit.restapi.client.ResourceFacade;
import org.ctoolkit.restapi.client.agent.model.ImportJobInfo;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrateJobMapSpecificationProvider
        implements MapSpecificationProvider, Serializable
{
    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private static final long serialVersionUID = 8477680668820034478L;

    @Inject
    private static Injector injector;

    private final MigrationJobConfiguration jobConfiguration;

    private final String agentUrl;

    private final String token;

    private final MigrateMapOnlyMapperJob mapper;

    /**
     * field injection to bypass serialization issue
     */
    @Inject
    private transient ResourceFacade facade;

    @Inject
    public MigrateJobMapSpecificationProvider( @Assisted MigrationJobConfiguration jobConfiguration,
                                               @Assisted( "agentUrl" ) String agentUrl,
                                               @Assisted( "token" ) String token,
                                               MigrateMapOnlyMapperJob mapper )
    {
        this.jobConfiguration = jobConfiguration;
        this.agentUrl = agentUrl;
        this.token = token;
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
                injector.injectMembers( MigrateJobMapSpecificationProvider.this );

                // start job to import data
                RequestCredential credential = new RequestCredential();
                credential.setApiKey( token );
                credential.setEndpointUrl( agentUrl );

                try
                {
                    Identifier parent = new Identifier( jobConfiguration.getImportId() );
                    facade.insert( new ImportJobInfo(), parent ).config( credential ).execute();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( "Unable to start import job", e );
                }

                return null;
            }
        } ).setJobName( "MigrationJob" ).build();
    }
}
