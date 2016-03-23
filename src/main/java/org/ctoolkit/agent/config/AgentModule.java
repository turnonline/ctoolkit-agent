package org.ctoolkit.agent.config;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.impl.PipelineServiceImpl;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.googlecode.objectify.ObjectifyService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import org.ctoolkit.agent.annotation.ImportJob;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.ChangeSetServiceBean;
import org.ctoolkit.agent.service.impl.datastore.DataAccessBean;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.ImportJobMapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.ImportMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.agent.service.impl.datastore.MapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeSetEntityToEntityMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.EntityFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportToImportMetadataMapper;
import org.ctoolkit.agent.service.impl.event.AuditEvent;

import javax.inject.Singleton;


/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        install( new FactoryModuleBuilder()
                .implement( MapSpecificationProvider.class, ImportJob.class, ImportJobMapSpecificationProvider.class )
                .build( JobSpecificationFactory.class ) );

        bind( EntityPool.class ).in( Singleton.class );
        bind( DataAccess.class ).to( DataAccessBean.class ).in( Singleton.class );
        bind( ChangeSetService.class ).to( ChangeSetServiceBean.class ).in( Singleton.class );
        bind( ChangeSetEntityToEntityMapper.class ).in( Singleton.class );
        bind( EntityEncoder.class ).in( Singleton.class );
        bind( ImportMapOnlyMapperJob.class ).in( Singleton.class );

        bind( PipelineService.class ).to( PipelineServiceImpl.class ).in( Singleton.class );
        bind( EventBus.class ).in( Singleton.class );
        bind( AuditEvent.class ).in( Singleton.class );

        ObjectifyService.register( ImportMetadata.class );
        ObjectifyService.register( ImportMetadataItem.class );

        requestStaticInjection( ImportMapOnlyMapperJob.class );
    }

    @Provides
    @Singleton
    public DatastoreService provideDatastoreService()
    {
        return DatastoreServiceFactory.getDatastoreService();
    }

    @Provides
    @Singleton
    public MapperFactory provideMapperFactory()
    {
        return new DefaultMapperFactory.Builder()
                .dumpStateOnException( false )
                .mapNulls( false )
                .useBuiltinConverters( true )
                .build();
    }

    @Provides
    @Singleton
    public MapperFacade provideMapperFacade( MapperFactory factory,
                                             ChangeSetEntityToEntityMapper changeSetEntityToEntityMapper,
                                             EntityFactory entityFactory,
                                             ImportToImportMetadataMapper importToImportMetadataMapper,
                                             ImportMetadataFactory importMetadataFactory )
    {
        factory.registerMapper( changeSetEntityToEntityMapper );
        factory.registerMapper( importToImportMetadataMapper );
        factory.registerObjectFactory( entityFactory, TypeFactory.valueOf( Entity.class ) );
        factory.registerObjectFactory( importMetadataFactory, TypeFactory.valueOf( ImportMetadata.class ) );

        return factory.getMapperFacade();
    }

    @Provides
    @Singleton
    public MapReduceSettings getSettings()
    {
        return new MapReduceSettings.Builder()
                .setBucketName( null )
                .setWorkerQueueName( "ctoolkit-agent" )
                .setModule( "ctoolkit-agent" )
                .build();
    }
}
