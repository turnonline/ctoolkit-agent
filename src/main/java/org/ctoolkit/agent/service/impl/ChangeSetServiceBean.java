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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.googlecode.objectify.VoidWork;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.annotation.EntityMarker;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.exception.ObjectNotFoundException;
import org.ctoolkit.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.MetadataAudit.Action;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.resource.ChangeJob;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.resource.ChangeSetEntity;
import org.ctoolkit.agent.resource.ChangeSetModelKindOp;
import org.ctoolkit.agent.resource.ChangeSetModelKindPropOp;
import org.ctoolkit.agent.resource.ExportJob;
import org.ctoolkit.agent.resource.ImportJob;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.dataflow.ImportBatchTask;
import org.ctoolkit.agent.service.impl.event.Auditable;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Implementation of {@link ChangeSetService}
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ChangeSetServiceBean
        implements ChangeSetService
{
    private final Dataflow dataflow;

    private final DataAccess dataAccess;

    private final Storage storage;

    private final Set<String> systemKinds = new HashSet<>();

    private final Map<Class, Provider> jobInfoProviders = new HashMap<>();

    private final String bucketName;

    private final String projectId;

    @Inject
    public ChangeSetServiceBean( @Nullable Dataflow dataflow,
                                 DataAccess dataAccess,
                                 Storage storage,
                                 @BucketName String bucketName,
                                 @ProjectId String projectId )
    {
        this.dataflow = dataflow;
        this.dataAccess = dataAccess;
        this.storage = storage;
        this.bucketName = bucketName;
        this.projectId = projectId;

        systemKinds.add( "MR-IncrementalTask" );
        systemKinds.add( "MR-ShardedJob" );
        systemKinds.add( "MR-ShardRetryState" );
        systemKinds.add( "pipeline-barrier" );
        systemKinds.add( "pipeline-fanoutTask" );
        systemKinds.add( "pipeline-job" );
        systemKinds.add( "pipeline-jobInstanceRecord" );
        systemKinds.add( "pipeline-slot" );
        systemKinds.add( "pipeline-exception" );

        systemKinds.add( "__BlobInfo__" );
        systemKinds.add( "__GsFileInfo__" );
        systemKinds.add( "_ah_FakeCloudStorage__app_default_bucket" );

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

        systemKinds.add( "CounterShard_" );

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
        jobInfoProviders.put( ChangeMetadata.class, new Provider<JobInfo>()
        {
            @Override
            public JobInfo get()
            {
                return new ChangeJob();
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

        com.google.cloud.datastore.Key key = com.google.cloud.datastore.Key.newBuilder( projectId, kind, id ).build();
        return dataAccess.find( metadataKey.getMetadataClass(), key );
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
        return dataAccess.find( filter );
    }

    // ------------------------------------------
    // -- metadata item
    // ------------------------------------------

    @Override
    @Auditable( action = Action.CREATE )
    // TODO: refactor to cloud datastore
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI create( final M metadata, final MI metadataItem )
    {
        // store again to persist file name
        //dataAccess.create( metadataItem );

        final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        // update parent - in transaction because multiple threads can access parent
        ofy().transactNew( 100, new VoidWork()
        {
            @Override
            public void vrun()
            {
                Key parentKey = KeyFactory.stringToKey( metadata.getKey() );

                try
                {
                    Entity metadataEntity = datastoreService.get( parentKey );
                    List<Key> items = ( List<Key> ) metadataEntity.getProperty( "itemsRef" );
                    if ( items == null )
                    {
                        items = new ArrayList<>();
                    }
                    Key itemKey = KeyFactory.stringToKey( metadataItem.getKey() );
                    if ( !items.contains( itemKey ) )
                    {
                        items.add( itemKey );
                    }

                    metadataEntity.setUnindexedProperty( "itemsCount", items.size() );
                    metadataEntity.setUnindexedProperty( "itemsRef", items );
                    datastoreService.put( ofy().getTransaction(), metadataEntity );
                }
                catch ( EntityNotFoundException e )
                {
                    throw new RuntimeException( "Parent for item not found: " + parentKey, e );
                }
            }
        } );

        if ( metadataItem.getData() != null )
        {
//            storageService.store(
//                    metadataItem.getData(),
//                    metadataItem.getDataType().mimeType(),
//                    metadataItem.newFileName(),
//                    bucketName
//            );
        }

        // store again to persist file name
//        dataAccess.update( metadataItem );

        return metadataItem;
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
        MI item = dataAccess.find( metadataItemKey.getMetadataItemClass(), metadataItemKey.getKey() );

        if ( item.getFileName() != null )
        {
            byte[] data = storage.readAllBytes( BlobId.of( bucketName, item.getFileName() ) );
            item.setData( data );
        }

        return item;
    }

    @Override
    @Auditable( action = Action.DELETE )
    // TODO: refactor to cloud datastore
    public <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( final MI metadataItem )
    {
        if ( metadataItem.getFileName() != null )
        {
//            storageService.delete( metadataItem.getFileName(), bucketName );
        }

        ofy().transactNew( 5, new VoidWork()
        {

            @Override
            public void vrun()
            {
                M importMetadata = metadataItem.getMetadata();
                importMetadata.getItems().remove( metadataItem );
                importMetadata.save();
            }
        } );
    }

    // ------------------------------------------
    // -- job
    // ------------------------------------------

    @Override
    @Auditable( action = Action.START_JOB )
    public <M extends BaseMetadata> void startJob( M metadata ) throws ProcessAlreadyRunning
    {
        // TODO: check if job is running

        if ( metadata.getClass() == ImportMetadata.class )
        {
            ImportBatchTask task = new ImportBatchTask( metadata.getId() );
            task.run();
        }
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
                // TODO: try to obtain error if occured
            }
            catch ( Exception e )
            {
                e.printStackTrace();
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
    // TODO: refactor to datastore cloud
    public void importChangeSet( ChangeSet changeSet )
    {
        changeChangeSet( changeSet );
    }

    @Override
    // TODO: refactor to datastore cloud
    public void changeChangeSet( ChangeSet changeSet )
    {
        dataAccess.flushPool();

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
                        {
                            dataAccess.dropEntity( kindOp.getKind() );
                            break;
                        }
                        case ChangeSetModelKindOp.OP_CLEAN:
                        {
                            dataAccess.clearEntity( kindOp.getKind() );
                            break;
                        }
                        default:
                        {
                            throw new IllegalArgumentException( "Unsupported Kind operation! " + kindOp.getOp() );
                        }
                    }
                }
            }

            // process KindPropOps
            if ( changeSet.getModel().hasKindPropOpsObject() )
            {
                for ( ChangeSetModelKindPropOp kindPropOp : changeSet.getModel().getKindPropsOp() )
                {
                    switch ( kindPropOp.getOp() )
                    {
                        case ChangeSetModelKindPropOp.OP_ADD:
                        {
                            dataAccess.addEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getNewName(),
                                    kindPropOp.getNewType(),
                                    kindPropOp.getNewValue()
                            );
                            break;
                        }
                        case ChangeSetModelKindPropOp.OP_REMOVE:
                        {
                            dataAccess.removeEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getProperty()
                            );
                            break;
                        }
                        case ChangeSetModelKindPropOp.OP_CHANGE:
                        {
                            dataAccess.changeEntityProperty(
                                    kindPropOp.getKind(),
                                    kindPropOp.getProperty(),
                                    kindPropOp.getNewName(),
                                    kindPropOp.getNewType(),
                                    kindPropOp.getNewValue()
                            );
                            break;
                        }
                        default:
                        {
                            throw new IllegalArgumentException( "Unsupported Kind prop operation! " + kindPropOp.getOp() );
                        }
                    }
                }
            }
        }

        // apply entity changes
        if ( changeSet.hasEntities() )
        {
            for ( ChangeSetEntity cse : changeSet.getEntities().getEntity() )
            {
                dataAccess.addEntity( cse );
            }
        }

        dataAccess.flushPool();
    }

    // ------------------------------------------
    // -- meta info
    // ------------------------------------------

    @Override
    // TODO: refactor to datastore cloud
    public ChangeSet exportChangeSet( String entity )
    {
        return dataAccess.exportChangeSet( entity );
    }

    // ------------------------------------------
    // -- audits
    // ------------------------------------------

    @Override
    // TODO: refactor to datastore cloud
    public List<MetadataAudit> list( AuditFilter filter )
    {
        return dataAccess.find( filter );
    }

    // ------------------------------------------
    // -- meta infos
    // ------------------------------------------

    @Override
    // TODO: refactor to datastore cloud
    public List<KindMetaData> kinds()
    {
        List<KindMetaData> kinds = dataAccess.kinds();
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
    // TODO: refactor to datastore cloud
    public List<PropertyMetaData> properties( String kind )
    {
        return dataAccess.properties( kind );
    }
}
