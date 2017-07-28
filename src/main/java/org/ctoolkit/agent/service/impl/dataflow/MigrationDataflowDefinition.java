package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.PipelineResult;
import com.google.cloud.dataflow.sdk.io.datastore.CustomReadFn;
import com.google.cloud.dataflow.sdk.io.datastore.CustomSplitQuery;
import com.google.cloud.dataflow.sdk.runners.DataflowPipelineJob;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.Flatten;
import com.google.cloud.dataflow.sdk.transforms.GroupByKey;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.dataflow.sdk.transforms.Values;
import com.google.cloud.dataflow.sdk.values.KV;
import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import com.google.cloud.datastore.ListValue;
import com.google.common.base.Charsets;
import com.google.datastore.v1.Entity;
import com.google.datastore.v1.Query;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.MigrationMetadataItem;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.resource.MigrationSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.impl.dataflow.shared.BaseDataflowDefinition;
import org.ctoolkit.agent.service.impl.dataflow.shared.LoadItems;
import org.ctoolkit.agent.util.XmlUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Migration dataflow definition
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MigrationDataflowDefinition
        extends BaseDataflowDefinition<MigrationMetadata>
{
    public MigrationDataflowDefinition()
    {
        super();
    }

    public MigrationDataflowDefinition( Long metadataId )
    {
        super( metadataId, MigrationMetadata.class );
    }

    @Override
    public void run()
    {
        super.run();

        String kind = MigrationMetadata.class.getAnnotation( EntityMarker.class ).name();
        Key key = Key.newBuilder( projectId, kind, metadataId ).build();

        // reset metadata counters and state
        resetMetadata( key );

        // create pipeline
        final Pipeline pipeline = Pipeline.create( pipelineOptions );

        // define pipelines
        pipeline
                .apply( "Load migration items", new LoadItems<>( key, clazz, datastore ) )
                .apply( "Prepare operations", ParDo.of( new DoFn<KeyValue, KV<String, MigrationSetKindOperation>>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        ChangeSetService changeSetService = injector().getInstance( ChangeSetService.class );
                        MigrationMetadataItem item = changeSetService.get( new MetadataItemKey<>( MigrationMetadataItem.class, c.element().get() ) );

                        MigrationSet migrationSet = item.unmarshallData( MigrationSet.class );

                        // add all types
                        for ( MigrationSetKindOperation op : migrationSet.getOperations().getAll() )
                        {
                            c.output( KV.of( op.getKind(), op ) );
                        }
                    }
                } ) )
                .apply( "Group operations", GroupByKey.<String, MigrationSetKindOperation>create() )
                .apply( "Prepare kind queries", ParDo.of( new DoFn<KV<String, Iterable<MigrationSetKindOperation>>, Query>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        String kind = c.element().getKey();
                        String xmlRoot = "";
                        List<BlobValue> kindOpsList = new ArrayList<>();


                        for ( MigrationSetKindOperation op : c.element().getValue() )
                        {
                            kindOpsList.add( BlobValue.of( Blob.copyFrom( XmlUtils.marshall( op ).getBytes( Charsets.UTF_8 ) ) ) );
                            xmlRoot = op.getClass().getName();
                        }

                        // create _MigrationKindGroup entity which stores 'state' of kind operations to further dataflow processing
                        com.google.cloud.datastore.Entity.Builder builder = com.google.cloud.datastore.Entity.newBuilder( migrationKindGroupKey( kind ) );
                        builder.set( "operations", new ListValue( kindOpsList ) );
                        builder.set( "xmlRoot", xmlRoot );
                        datastore().put( builder.build() );

                        // create query splits
                        Query.Builder queryBuilder = Query.newBuilder();
                        queryBuilder.addKindBuilder().setName( kind );
                        Query query = queryBuilder.build();

                        c.output( query );
                    }
                } ) )
                .apply( "Split queries", ParDo.of( new CustomSplitQuery( projectId ) ) )
                .apply( "Group splitted queries by unique ID", GroupByKey.<Integer, Query>create() )
                .apply( "Create values from split groups", Values.<Iterable<Query>>create() )
                .apply( "Flatten split group values", Flatten.<Query>iterables() )
                .apply( "Read entities from split queries", ParDo.of( new CustomReadFn( projectId ) ) )
                .apply( "Migrate entity", ParDo.of( new DoFn<Iterable<Entity>, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        ChangeSetService changeSetService = injector().getInstance( ChangeSetService.class );

                        for (Entity entityPb : c.element())
                        {
                            // load list of operations
                            com.google.datastore.v1.Key key = entityPb.getKey();
                            String kind = key.getPath( key.getPathCount() - 1 ).getKind();
                            com.google.cloud.datastore.Entity entity = datastore().get( migrationKindGroupKey( kind ) );

                            List<BlobValue> ops = entity.getList( "operations" );
                            Class xmlRoot = Class.forName( entity.getString( "xmlRoot" ) );

                            // migrate entity for every element in operation list
                            com.google.cloud.datastore.Entity entityToMigrate = fromPb( entityPb );

                            for ( BlobValue blobValue : ops )
                            {
                                MigrationSetKindOperation operation = ( MigrationSetKindOperation ) XmlUtils.unmarshall( blobValue.get().asInputStream(), xmlRoot );
                                changeSetService.migrate( operation, entityToMigrate );
                            }
                        }

                        // flush unflushed entities
                        changeSetService.flushPool();
                    }
                } ) );

        PipelineResult result = pipeline.run();

        if ( result instanceof DataflowPipelineJob )
        {
            DataflowPipelineJob jobResult = ( DataflowPipelineJob ) result;

            MigrationMetadata metadata = ModelConverter.convert( MigrationMetadata.class, datastore.get( key ) );
            metadata.setJobId( jobResult.getJobId() );
            metadata.save();
        }
    }

    /**
     * Create migration kind group Key
     *
     * @param kind entity kind
     * @return {@link Key}
     */
    private Key migrationKindGroupKey( String kind )
    {
        String projectId = injector().getInstance( com.google.inject.Key.get( String.class, ProjectId.class ) );
        return Key.newBuilder( projectId, "_MigrationKindGroup", kind ).build();
    }

    /**
     * // TODO: this is hack. Hopefully #fromPb method will be made visible in future versions
     *
     * Convert low level v1 Entity to com.google.cloud.datastore.Entity wrapper.
     * This is just workaround because com.google.cloud.datastore.Entity#fromPb method is package private
     *
     * @param entityPb {@link Entity}
     * @return converted com.google.cloud.datastore.Entity
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static com.google.cloud.datastore.Entity fromPb( Entity entityPb )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = null;
        try
        {
            method = com.google.cloud.datastore.Entity.class.getDeclaredMethod( "fromPb", Entity.class );
            method.setAccessible( true );
            return ( com.google.cloud.datastore.Entity ) method.invoke( null, entityPb );
        }
        finally
        {
            if ( method != null )
            {
                method.setAccessible( false );
            }
        }
    }
}
