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
import org.ctoolkit.agent.annotation.ChangeJob;
import org.ctoolkit.agent.annotation.ExportJob;
import org.ctoolkit.agent.annotation.ImportJob;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.impl.ChangeSetServiceBean;
import org.ctoolkit.agent.service.impl.datastore.ChangeJobMapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.ChangeMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.DataAccessBean;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.ExportJobMapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.ExportMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.ImportJobMapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.ImportMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.JobSpecificationFactory;
import org.ctoolkit.agent.service.impl.datastore.MapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeSetEntityToEntityMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeToChangeMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.EntityFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportToExportMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportToImportMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.rule.ChangeRuleEngine;
import org.ctoolkit.agent.service.impl.datastore.rule.NewNameChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewNameNewTypeChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewNameNewTypeNewValueChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewNameNewValueChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewTypeChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewTypeNewValueChangeRule;
import org.ctoolkit.agent.service.impl.datastore.rule.NewValueChangeRule;
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
                .implement( MapSpecificationProvider.class, ChangeJob.class, ChangeJobMapSpecificationProvider.class )
                .implement( MapSpecificationProvider.class, ExportJob.class, ExportJobMapSpecificationProvider.class )
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

        bind( ChangeRuleEngine.class ).in( Singleton.class );
        bind( NewNameChangeRule.class ).in( Singleton.class );
        bind( NewTypeChangeRule.class ).in( Singleton.class );
        bind( NewValueChangeRule.class ).in( Singleton.class );
        bind( NewNameNewTypeChangeRule.class ).in( Singleton.class );
        bind( NewNameNewValueChangeRule.class ).in( Singleton.class );
        bind( NewTypeNewValueChangeRule.class ).in( Singleton.class );
        bind( NewNameNewTypeNewValueChangeRule.class ).in( Singleton.class );

        ObjectifyService.register( ImportMetadata.class );
        ObjectifyService.register( ImportMetadataItem.class );
        ObjectifyService.register( ChangeMetadata.class );
        ObjectifyService.register( ChangeMetadataItem.class );
        ObjectifyService.register( ExportMetadata.class );
        ObjectifyService.register( ExportMetadataItem.class );

        requestStaticInjection( ImportMapOnlyMapperJob.class );
        requestStaticInjection( ChangeMapOnlyMapperJob.class );
        requestStaticInjection( ExportMapOnlyMapperJob.class );
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
                                             // mappers
                                             ChangeSetEntityToEntityMapper changeSetEntityToEntityMapper,
                                             ImportToImportMetadataMapper importToImportMetadataMapper,
                                             ExportToExportMetadataMapper exportToExportMetadataMapper,
                                             ChangeToChangeMetadataMapper changeToChangeMetadataMapper,
                                             // factories
                                             EntityFactory entityFactory,
                                             ImportMetadataFactory importMetadataFactory,
                                             ExportMetadataFactory exportMetadataFactory,
                                             ChangeMetadataFactory changeMetadataFactory )
    {
        // register custom mappers
        factory.registerMapper( changeSetEntityToEntityMapper );
        factory.registerMapper( importToImportMetadataMapper );
        factory.registerMapper( exportToExportMetadataMapper );
        factory.registerMapper( changeToChangeMetadataMapper );

        // register factories
        factory.registerObjectFactory( entityFactory, TypeFactory.valueOf( Entity.class ) );
        factory.registerObjectFactory( importMetadataFactory, TypeFactory.valueOf( ImportMetadata.class ) );
        factory.registerObjectFactory( exportMetadataFactory, TypeFactory.valueOf( ExportMetadata.class ) );
        factory.registerObjectFactory( changeMetadataFactory, TypeFactory.valueOf( ChangeMetadata.class ) );

        return factory.getMapperFacade();
    }

    @Provides
    @Singleton
    public MapReduceSettings provideMapReduceSettings()
    {
        return new MapReduceSettings.Builder()
                .setBucketName( null )
                .setWorkerQueueName( "ctoolkit-agent" )
                .setModule( "ctoolkit-agent" )
                .build();
    }
}
