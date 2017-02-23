package org.ctoolkit.agent.config;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.impl.PipelineServiceImpl;
import com.google.common.eventbus.EventBus;
import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;
import com.googlecode.objectify.ObjectifyService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import net.oauth.jsontoken.Checker;
import org.ctoolkit.agent.annotation.ChangeJob;
import org.ctoolkit.agent.annotation.ExportJob;
import org.ctoolkit.agent.annotation.ImportJob;
import org.ctoolkit.agent.annotation.MigrateJob;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ChangeMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ExportMetadataItem;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.ImportMetadataItem;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.rest.IAMAuthenticator;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.agent.service.impl.ChangeSetServiceBean;
import org.ctoolkit.agent.service.impl.RestContextThreadLocal;
import org.ctoolkit.agent.service.impl.datastore.AuditInterceptor;
import org.ctoolkit.agent.service.impl.datastore.AuditSubscription;
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
import org.ctoolkit.agent.service.impl.datastore.MigrateJobMapSpecificationProvider;
import org.ctoolkit.agent.service.impl.datastore.MigrateMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeItemToChangeMetadataItemMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeMetadataItemFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeSetEntityToEntityMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeToChangeMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.EntityFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportToExportMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportItemToImportMetadataItemMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportMetadataItemFactory;
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
import org.ctoolkit.agent.service.impl.event.Auditable;
import org.ctoolkit.restapi.client.appengine.FacadeAppEngineModule;
import org.ctoolkit.restapi.client.identity.verifier.IdentityVerifierModule;
import org.ctoolkit.services.common.CtoolkitCommonServicesModule;
import org.ctoolkit.services.guice.appengine.CtoolkitServicesAppEngineModule;
import org.ctoolkit.services.storage.appengine.CtoolkitServicesAppEngineStorageModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.security.SignatureException;


/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentModule
        extends AbstractModule
{

    public static final String BUCKET_NAME = "bucketName";

    @Override
    protected void configure()
    {
        // install map reduce job specification providers factory
        install( new FactoryModuleBuilder()
                .implement( MapSpecificationProvider.class, ImportJob.class, ImportJobMapSpecificationProvider.class )
                .implement( MapSpecificationProvider.class, ChangeJob.class, ChangeJobMapSpecificationProvider.class )
                .implement( MapSpecificationProvider.class, ExportJob.class, ExportJobMapSpecificationProvider.class )
                .implement( MapSpecificationProvider.class, MigrateJob.class, MigrateJobMapSpecificationProvider.class )
                .build( JobSpecificationFactory.class ) );

        // install ctoolkit agent provider factory
        install( new FactoryModuleBuilder()
                .implement( CtoolkitAgentProvider.class, CtoolkitAgentProviderBean.class )
                .build( CtoolkitAgentFactory.class )
        );

        install( new FacadeAppEngineModule() );
        install( new IdentityVerifierModule() );
        install( new CtoolkitServicesAppEngineModule() );
        install( new CtoolkitServicesAppEngineStorageModule() );
        install( new CtoolkitCommonServicesModule() );

        bind( Checker.class ).to( AudienceChecker.class );
        bind( EntityPool.class ).in( Singleton.class );
        bind( DataAccess.class ).to( DataAccessBean.class ).in( Singleton.class );
        bind( ChangeSetService.class ).to( ChangeSetServiceBean.class ).in( Singleton.class );
        bind( ChangeSetEntityToEntityMapper.class ).in( Singleton.class );
        bind( EntityEncoder.class ).in( Singleton.class );
        bind( ImportMapOnlyMapperJob.class ).in( Singleton.class );
        bind( PipelineService.class ).to( PipelineServiceImpl.class ).in( Singleton.class );

        bind( EventBus.class ).in( Singleton.class );
        bind( AuditEvent.class ).in( Singleton.class );
        bind( AuditSubscription.class ).asEagerSingleton();
        AuditInterceptor auditInterceptor = new AuditInterceptor();
        requestInjection( auditInterceptor );
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith( Auditable.class ),
                auditInterceptor
        );

        bind( ChangeRuleEngine.class ).in( Singleton.class );
        bind( NewNameChangeRule.class ).in( Singleton.class );
        bind( NewTypeChangeRule.class ).in( Singleton.class );
        bind( NewValueChangeRule.class ).in( Singleton.class );
        bind( NewNameNewTypeChangeRule.class ).in( Singleton.class );
        bind( NewNameNewValueChangeRule.class ).in( Singleton.class );
        bind( NewTypeNewValueChangeRule.class ).in( Singleton.class );
        bind( NewNameNewTypeNewValueChangeRule.class ).in( Singleton.class );

        bind( RestContext.class ).to( RestContextThreadLocal.class ).in( RequestScoped.class );

        ObjectifyService.register( ImportMetadata.class );
        ObjectifyService.register( ImportMetadataItem.class );
        ObjectifyService.register( ChangeMetadata.class );
        ObjectifyService.register( ChangeMetadataItem.class );
        ObjectifyService.register( ExportMetadata.class );
        ObjectifyService.register( ExportMetadataItem.class );
        ObjectifyService.register( MetadataAudit.class );

        requestStaticInjection( ImportMapOnlyMapperJob.class );
        requestStaticInjection( ChangeMapOnlyMapperJob.class );
        requestStaticInjection( ExportMapOnlyMapperJob.class );
        requestStaticInjection( MigrateMapOnlyMapperJob.class );
        requestStaticInjection( MigrateJobMapSpecificationProvider.class );
        requestStaticInjection( IAMAuthenticator.class );
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

                                             ImportItemToImportMetadataItemMapper importItemToImportMetadataItemMapper,
                                             ChangeItemToChangeMetadataItemMapper changeItemToChangeMetadataItemMapper,

                                             // factories
                                             EntityFactory entityFactory,
                                             ImportMetadataFactory importMetadataFactory,
                                             ExportMetadataFactory exportMetadataFactory,
                                             ChangeMetadataFactory changeMetadataFactory,

                                             ImportMetadataItemFactory importMetadataItemFactory,
                                             ChangeMetadataItemFactory changeMetadataItemFactory )
    {
        // register custom mappers
        factory.registerMapper( changeSetEntityToEntityMapper );
        factory.registerMapper( importToImportMetadataMapper );
        factory.registerMapper( exportToExportMetadataMapper );
        factory.registerMapper( changeToChangeMetadataMapper );

        factory.registerMapper( importItemToImportMetadataItemMapper );
        factory.registerMapper( changeItemToChangeMetadataItemMapper );

        // register factories
        factory.registerObjectFactory( entityFactory, TypeFactory.valueOf( Entity.class ) );
        factory.registerObjectFactory( importMetadataFactory, TypeFactory.valueOf( ImportMetadata.class ) );
        factory.registerObjectFactory( exportMetadataFactory, TypeFactory.valueOf( ExportMetadata.class ) );
        factory.registerObjectFactory( changeMetadataFactory, TypeFactory.valueOf( ChangeMetadata.class ) );

        factory.registerObjectFactory( importMetadataItemFactory, TypeFactory.valueOf( ImportMetadataItem.class ) );
        factory.registerObjectFactory( changeMetadataItemFactory, TypeFactory.valueOf( ChangeMetadataItem.class ) );

        return factory.getMapperFacade();
    }

    @Provides
    @Singleton
    public MapReduceSettings provideMapReduceSettings()
    {
        return new MapReduceSettings.Builder()
                .setWorkerQueueName( "ctoolkit-agent" )
                .build();
    }

    @Provides
    @Singleton
    @Named( BUCKET_NAME )
    public String provideBucketName()
    {
        return AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName();
    }

    private static class AudienceChecker
            implements Checker
    {
        private AudienceChecker()
        {
        }

        @Override
        public void check( JsonObject payload ) throws SignatureException
        {
            // TODO: should we check audience(project id)?
            // TODO: -audience can vary if user wants to export from one project to another
            // TODO: -what should be source of expected audiences - database?
        }
    }
}