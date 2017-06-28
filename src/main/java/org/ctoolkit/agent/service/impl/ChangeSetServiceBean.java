/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.service.impl;

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.NotImplementedException;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.MetadataAudit.Action;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.model.MigrationMetadata;
import org.ctoolkit.agent.model.ModelConverter;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.resource.ChangeSetModelKindOp;
import org.ctoolkit.agent.resource.ExportJob;
import org.ctoolkit.agent.resource.ImportJob;
import org.ctoolkit.agent.resource.MigrationJob;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.agent.service.impl.dataflow.ImportDataflowDefinition;
import org.ctoolkit.agent.service.impl.dataflow.MigrationDataflowDefinition;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.KeyProvider;
import org.ctoolkit.agent.service.impl.event.AuditEvent;
import org.ctoolkit.agent.service.impl.event.Auditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.cloud.datastore.StructuredQuery.OrderBy.asc;
import static com.google.cloud.datastore.StructuredQuery.OrderBy.desc;

/**
 * Implementation of {@link ChangeSetService}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBean
        implements ChangeSetService
{
    private static Logger log = LoggerFactory.getLogger( ChangeSetServiceBean.class );

    /**
     * The default number of entities to put.delete from the data store
     */
    // TODO: configurable
    private static final int DEFAULT_COUNT_LIMIT = 100;

    private final Dataflow dataflow;

    private final KeyProvider keyProvider;

    private final Datastore datastore;

    private final Storage storage;

    private final EntityPool pool;

    private final MapperFacade mapper;

    private final Provider<RestContext> restContextProvider;

    private final Set<String> systemKinds = new HashSet<>();

    private final Map<Class, Provider> jobInfoProviders = new HashMap<>();

    private final String bucketName;

    private final String projectId;

    @Inject
    public ChangeSetServiceBean( @Nullable Dataflow dataflow,
                                 KeyProvider keyProvider,
                                 Datastore datastore,
                                 Storage storage,
                                 EntityPool pool,
                                 MapperFacade mapper,
                                 @Nullable Provider<RestContext> restContextProvider,
                                 @BucketName String bucketName,
                                 @ProjectId String projectId )
    {
        this.dataflow = dataflow;
        this.keyProvider = keyProvider;
        this.datastore = datastore;
        this.storage = storage;
        this.pool = pool;
        this.mapper = mapper;
        this.restContextProvider = restContextProvider;
        this.bucketName = bucketName;
        this.projectId = projectId;

        systemKinds.add( "__Stat_Kind_IsRootEntity__" );
        systemKinds.add( "__Stat_Kind_NotRootEntity__" );
        systemKinds.add( "__Stat_Kind__" );
        systemKinds.add( "__Stat_PropertyName_Kind__" );
        systemKinds.add( "__Stat_PropertyType_Kind__" );
        systemKinds.add( "__Stat_PropertyType_PropertyName_Kind__" );
        systemKinds.add( "__Stat_PropertyType__" );
        systemKinds.add( "__Stat_Total__" );
        systemKinds.add( "__Stat_Kind_CompositeIndex__" );
        systemKinds.add( "_ah_SESSION" );

        systemKinds.add( "_ImportMetadata" );
        systemKinds.add( "_ImportMetadataItem" );
        systemKinds.add( "_ExportMetadata" );
        systemKinds.add( "_ExportMetadataItem" );
        systemKinds.add( "_ChangeMetadata" );
        systemKinds.add( "_ChangeMetadataItem" );
        systemKinds.add( "_MetadataAudit" );

        // init job info providers
        jobInfoProviders.put( ImportMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ImportJob();
            }
        } );
        jobInfoProviders.put( ExportMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ExportJob();
            }
        } );
        jobInfoProviders.put( MigrationMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new MigrationJob();
            }
        } );
    }

    // ------------------------------------------
    // -- metadata
    // ------------------------------------------

    @Override
    @Auditable( action = Action.CREATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M create( M metadata )
    {
        // create metadata
        metadata.save();
        return metadata;
    }

    @Override
    @Auditable( action = Action.UPDATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M update( M metadata )
    {
        // update metadata
        metadata.save();
        return metadata;
    }

    @Override
    public <M extends BaseMetadata> M get( MetadataKey<M> metadataKey )
    {
        String kind = metadataKey.getMetadataClass().getAnnotation( EntityMarker.class ).name();
        Long id = metadataKey.getId();

        Key key = Key.newBuilder( projectId, kind, id ).build();
        return ModelConverter.convert( metadataKey.getMetadataClass(), datastore.get( key ) );
    }

    @Override
    @Auditable( action = Action.DELETE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( M metadata )
    {
        metadata.delete();
    }

    @Override
    public <M extends BaseMetadata> List<M> list( BaseMetadataFilter<M> filter )
    {
        com.google.cloud.datastore.Query<Entity> query = com.google.cloud.datastore.Query.newEntityQueryBuilder()
                .setKind( filter.getMetadataClass().getAnnotation( EntityMarker.class ).name() )
                .setLimit( filter.getLength() )
                .setOffset( filter.getStart() )
                .addOrderBy( filter.isAscending() ? asc( filter.getOrderBy() ) : desc( filter.getOrderBy() ) )
                .build();

        List<M> list = new ArrayList<>();
        QueryResults<Entity> results = datastore.run( query );
        while ( results.hasNext() )
        {
            Entity entity = results.next();
            M metadata = ModelConverter.convert( filter.getMetadataClass(), entity );
            list.add( metadata );
        }

        return list;
    }

    // ------------------------------------------
    // -- metadata item
    // ------------------------------------------

    @Override
    @Auditable( action = Action.CREATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI create( final M metadata, final MI metadataItem )
    {
        throw new NotImplementedException( "Method not implemented" );
    }

    @Override
    @Auditable( action = Action.UPDATE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI update( MI metadataItem )
    {
        metadataItem.save();
        return metadataItem;
    }

    @Override
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI get( MetadataItemKey<M, MI> metadataItemKey )
    {
        MI item = ModelConverter.convert( metadataItemKey.getMetadataItemClass(), datastore.get( metadataItemKey.getKey() ) );

        if ( item.getFileName() != null )
        {
            byte[] data = storage.readAllBytes( BlobId.of( bucketName, item.getFileName() ) );
            item.setData( data );
        }

        return item;
    }

    @Override
    @Auditable( action = Action.DELETE )
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( final MI metadataItem )
    {
        throw new NotImplementedException( "Method not implemented" );
    }

    // ------------------------------------------
    // -- job
    // ------------------------------------------

    @Override
    @Auditable( action = Action.START_JOB )
    public <M extends BaseMetadata> void startJob( M metadata ) throws ProcessAlreadyRunning
    {
        try
        {
            Job job = dataflow.projects().jobs().get( projectId, metadata.getJobId() ).execute();
            if ( JobState.RUNNING.toDataflowState().equals( job.getCurrentState() ) )
            {
                throw new ProcessAlreadyRunning( "Job with id '" + metadata.getJobId() + "' is already running. Wait until job is finished or cancel existing job." );
            }
        }
        catch ( IOException e )
        {
            throw new ObjectNotFoundException( "Unable to get job status. Job will not start.", e );
        }

        Runnable definition;

        if ( metadata.getClass() == ImportMetadata.class )
        {
            definition = new ImportDataflowDefinition( metadata.getId() );
        }
        else if ( metadata.getClass() == MigrationMetadata.class )
        {
            definition = new MigrationDataflowDefinition( metadata.getId() );
        }
        else
        {
            throw new ObjectNotFoundException( "Unexpected metadata class '" + metadata.getClass() + "'." );
        }

        definition.run();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <JI extends JobInfo, M extends BaseMetadata> JI getJobInfo( M metadata )
    {
        JI jobInfo = ( JI ) jobInfoProviders.get( metadata.getClass() ).get();
        jobInfo.setId( metadata.getId() );
        jobInfo.setJobId( metadata.getJobId() );
        jobInfo.setJobUrl( metadata.getJobUrl() );
        jobInfo.setProcessedItems( metadata.getProcessedOkItems() );
        jobInfo.setProcessedErrorItems( metadata.getProcessedErrorItems() );
        jobInfo.setTotalItems( metadata.getItemsCount() );

        if ( metadata.getJobId() != null )
        {
            try
            {
                Job job = dataflow.projects().jobs().get( projectId, metadata.getJobId() ).execute();
                jobInfo.setState( JobState.valueOf( job.getCurrentState().replace( "JOB_STATE_", "" ) ) );
            }
            catch ( Exception e )
            {
                log.error( "Error occur during getting job info", e );
                jobInfo.setState( JobState.UNKNOWN );
            }
        }

        return jobInfo;
    }

    @Override
    @Auditable( action = Action.CANCEL_JOB )
    public <M extends BaseMetadata> void cancelJob( M metadata )
    {
        if ( metadata.getJobId() == null )
        {
            throw new ObjectNotFoundException( "Map reduce job not created yet for: " + metadata );
        }

        try
        {
            Job content = new Job();
            content.setProjectId( projectId );
            content.setId( metadata.getJobId() );
            content.setRequestedState( JobState.CANCELLED.toDataflowState() );

            dataflow.projects().jobs().update( projectId, metadata.getJobId(), content ).execute();
        }
        catch ( IOException e )
        {
            throw new ObjectNotFoundException( "Unable to cancel job for id: " + metadata.getJobId(), e );
        }
    }

    // ------------------------------------------
    // -- changesets
    // ------------------------------------------

    @Override
    public void importChangeSet( ChangeSet changeSet )
    {
        pool.flush();

        // apply model changes
        if ( changeSet.hasModelObject() )
        {
            // process KindOps
            if ( changeSet.getModel().hasKindOpsObject() )
            {
                for ( ChangeSetModelKindOp kindOp : changeSet.getModel().getKindOp() )
                {
                    switch ( kindOp.getOp() )
                    {
                        case ChangeSetModelKindOp.OP_DROP:
                        case ChangeSetModelKindOp.OP_CLEAN:
                        {
                            while ( true )
                            {
                                KeyQuery query = KeyQuery.newKeyQueryBuilder()
                                        .setKind( kindOp.getKind() )
                                        .setLimit( DEFAULT_COUNT_LIMIT )
                                        .build();

                                QueryResults<Key> results = datastore.run( query );
                                int items = 0;

                                while ( results.hasNext() )
                                {
                                    pool.delete( results.next() );
                                    items++;
                                }

                                if ( items < DEFAULT_COUNT_LIMIT )
                                {
                                    pool.flush();
                                    break; // break while cycle - no more items to process
                                }
                            }

                            pool.flush();
                            break;
                        }
                        default:
                        {
                            throw new IllegalArgumentException( "Unsupported Kind operation! " + kindOp.getOp() );
                        }
                    }
                }
            }
        }

        // apply entity changes
        if ( changeSet.hasEntities() )
        {
            for ( ChangeSetEntity csEntity : changeSet.getEntities().getEntity() )
            {
                Entity entity = mapper.map( csEntity, Entity.Builder.class ).build();
                pool.put( entity );
            }
        }

        pool.flush();
    }

    // ------------------------------------------
    // -- meta info
    // ------------------------------------------

    @Override
    public ChangeSet exportChangeSet( String entity )
    {
        throw new NotImplementedException( "Method not implemented yet" );
    }

    @Override
    public void create( AuditEvent event )
    {
        Entity.Builder builder = Entity.newBuilder( keyProvider.key( new MetadataAudit() ) );

        builder.set( "ownerId", event.getOwner().getId() ); // TODO: solve owner key
        builder.set( "action", event.getAction().name() );
        builder.set( "operation", event.getOperation().name() );
        builder.set( "createDate", Timestamp.of( new Date() ) );
        builder.set( "createdBy", restContextProvider.get().getUserEmail() );
        builder.set( "userPhotoUrl", restContextProvider.get().getPhotoUrl() );
        builder.set( "userDisplayName", restContextProvider.get().getDisplayName() );

        datastore.put( builder.build() );
    }

    // ------------------------------------------
    // -- audits
    // ------------------------------------------

    @Override
    public List<MetadataAudit> list( AuditFilter filter )
    {
        EntityQuery.Builder queryBuilder = Query.newEntityQueryBuilder()
                .setKind( MetadataAudit.class.getAnnotation( EntityMarker.class ).name() )
                .setLimit( filter.getLength() )
                .setOffset( filter.getStart() );

        if ( filter.getOrderBy() != null )
        {
            queryBuilder.addOrderBy( filter.isAscending() ? asc( filter.getOrderBy() ) : desc( filter.getOrderBy() ) );
        }

        if ( filter.getOwnerId() != null )
        {
            queryBuilder.setFilter( PropertyFilter.eq( "ownerId", filter.getOwnerId() ) );
        }

        List<MetadataAudit> list = new ArrayList<>();
        QueryResults<Entity> results = datastore.run( queryBuilder.build() );
        while ( results.hasNext() )
        {
            list.add( ModelConverter.convert( MetadataAudit.class, results.next() ) );
        }

        return list;

    }

    // ------------------------------------------
    // -- meta infos
    // ------------------------------------------

    @Override
    public List<KindMetaData> kinds()
    {
        List<KindMetaData> kinds = new ArrayList<>();

        EntityQuery query = Query
                .newEntityQueryBuilder()
                .setKind( "__kind__" )
                .build();
        QueryResults<Entity> results = datastore.run( query );

        while ( results.hasNext() )
        {
            Entity e = results.next();

            KindMetaData kind = new KindMetaData();
            kind.setKind( e.getKey().getName() );
            kind.setNamespace( e.getKey().getNamespace() );
            kinds.add( kind );
        }

        Iterable<KindMetaData> result = Iterables.filter( kinds, new Predicate<KindMetaData>()
        {
            @Override
            public boolean apply( @Nullable KindMetaData input )
            {
                for ( String systemKind : systemKinds )
                {
                    if ( input.getKind().startsWith( systemKind ) )
                    {
                        return false;
                    }
                }

                return true;
            }
        } );

        return Lists.newArrayList( result );
    }

    @Override
    public List<PropertyMetaData> properties( String kind )
    {
        List<PropertyMetaData> properties = new ArrayList<>();

        Key ancestorKey = datastore.newKeyFactory().setKind( "__kind__" ).newKey( kind );

        EntityQuery query = Query
                .newEntityQueryBuilder()
                .setKind( "__property__" )
                .setFilter( PropertyFilter.hasAncestor( ancestorKey ) )
                .build();
        QueryResults<Entity> results = datastore.run( query );

        // Build list of query results
        while ( results.hasNext() )
        {
            Entity e = results.next();

            PropertyMetaData property = new PropertyMetaData();
            property.setProperty( e.getKey().getName() );
            property.setType( e.getList( "property_representation" ).get( 0 ).get().toString().toLowerCase() );
            property.setKind( e.getKey().getParent().getName() );
            property.setNamespace( e.getKey().getNamespace() );
            properties.add( property );
        }

        return properties;
    }
}