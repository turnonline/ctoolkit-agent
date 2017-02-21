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
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeJobMapSpecificationProvider
        implements MapSpecificationProvider
{
    // TODO: configurable
    public static final int SHARD_COUNT = 10;

    private final String parentKey;

    private final ChangeMapOnlyMapperJob mapper;

    @Inject
    public ChangeJobMapSpecificationProvider( @Assisted String parentKey, ChangeMapOnlyMapperJob mapper )
    {
        this.parentKey = parentKey;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MapSpecification<Entity, Entity, Entity> get()
    {
        Query query = new Query( "_ChangeMetadataItem" ).setAncestor( KeyFactory.stringToKey( parentKey ) );
        DatastoreInput input = new DatastoreInput( query, SHARD_COUNT );

        return new MapSpecification.Builder<>( input, mapper, new NoOutput<Entity, Entity>() )
                .setJobName( "ChangeJob" )
                .build();
    }
}
