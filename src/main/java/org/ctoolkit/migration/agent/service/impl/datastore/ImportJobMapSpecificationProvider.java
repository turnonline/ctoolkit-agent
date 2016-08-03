package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.NoOutput;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportJobMapSpecificationProvider
        implements MapSpecificationProvider
{
    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private final String parentKey;

    private final ImportMapOnlyMapperJob mapper;

    @Inject
    public ImportJobMapSpecificationProvider( @Assisted String parentKey, ImportMapOnlyMapperJob mapper )
    {
        this.parentKey = parentKey;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MapSpecification<Entity, Entity, Entity> get()
    {
        Query query = new Query( "_ImportMetadataItem" ).setAncestor( KeyFactory.stringToKey( parentKey ) );
        DatastoreInput input = new DatastoreInput( query, SHARD_COUNT );

        return new MapSpecification.Builder<>( input, mapper, new NoOutput<Entity, Entity>() )
                .setJobName( "ImportJob" )
                .build();
    }
}
