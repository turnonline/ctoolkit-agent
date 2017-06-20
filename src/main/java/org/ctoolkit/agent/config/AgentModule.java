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

package org.ctoolkit.agent.config;

import com.google.api.services.dataflow.Dataflow;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.impl.PipelineServiceImpl;
import com.google.cloud.ServiceOptions;
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.runners.DataflowPipelineRunner;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import net.oauth.jsontoken.Checker;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.annotation.StagingLocation;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ChangeMetadata;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.rest.IAMAuthenticator;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.DataAccess;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.agent.service.impl.ChangeSetServiceBean;
import org.ctoolkit.agent.service.impl.RestContextThreadLocal;
import org.ctoolkit.agent.service.impl.dataflow.ImportBatchTask;
import org.ctoolkit.agent.service.impl.datastore.AuditInterceptor;
import org.ctoolkit.agent.service.impl.datastore.AuditSubscription;
import org.ctoolkit.agent.service.impl.datastore.DataAccessBean;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.EntityPoolThreadLocal;
import org.ctoolkit.agent.service.impl.datastore.ImportMapOnlyMapperJob;
import org.ctoolkit.agent.service.impl.datastore.KeyProvider;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeItemToChangeMetadataItemMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeSetEntityToEntityBuilderMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ChangeToChangeMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.EntityBuilderFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportMetadataFactory;
import org.ctoolkit.agent.service.impl.datastore.mapper.ExportToExportMetadataMapper;
import org.ctoolkit.agent.service.impl.datastore.mapper.ImportItemToImportMetadataItemMapper;
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
import org.ctoolkit.agent.service.impl.event.Auditable;
import org.ctoolkit.restapi.client.ApiCredential;
import org.ctoolkit.restapi.client.agent.CtoolkitApiAgentModule;
import org.ctoolkit.restapi.client.appengine.FacadeAppEngineModule;
import org.ctoolkit.restapi.client.identity.verifier.IdentityVerifierModule;
import org.ctoolkit.restapi.client.provider.AuthKeyProvider;
import org.ctoolkit.services.common.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SignatureException;


/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentModule
        extends AbstractModule
{
    public static final String CONFIG_JSON_CREDENTIALS = "ctoolkit.agent.jsonCredentials";

    private static final Logger logger = LoggerFactory.getLogger( AgentModule.class );

    @Override
    protected void configure()
    {
        // install map reduce job specification providers factory
//        install( new FactoryModuleBuilder()
//                .implement( MapSpecificationProvider.class, ImportJob.class, ImportJobMapSpecificationProvider.class )
//                .implement( MapSpecificationProvider.class, ChangeJob.class, ChangeJobMapSpecificationProvider.class )
//                .implement( MapSpecificationProvider.class, ExportJob.class, ExportJobMapSpecificationProvider.class )
//                .implement( MapSpecificationProvider.class, MigrateJob.class, MigrateJobMapSpecificationProvider.class )
//                .build( JobSpecificationFactory.class ) );

        install( new FacadeAppEngineModule() );
        install( new IdentityVerifierModule() );
//        install( new CtoolkitServicesAppEngineModule() );
//        install( new CtoolkitServicesAppEngineStorageModule() );
//        install( new CtoolkitCommonServicesModule() );
        install( new CtoolkitApiAgentModule() );

        // define api credential
        ApiCredential credential = new ApiCredential( CtoolkitApiAgentModule.API_PREFIX );
        // initialized as non null value, will be overridden by request credential
        credential.setEndpointUrl( "http://localhost:8999/_ah/api" );
        Names.bindProperties( binder(), credential );

        bind( AuthKeyProvider.class ).to( JsonAuthKeyProvider.class ).in( Singleton.class );
        bind( Checker.class ).to( AudienceChecker.class );
        bind( EntityPool.class ).to( EntityPoolThreadLocal.class );
        bind( DataAccess.class ).to( DataAccessBean.class ).in( Singleton.class );
        bind( ChangeSetService.class ).to( ChangeSetServiceBean.class ).in( Singleton.class );
        bind( ChangeSetEntityToEntityBuilderMapper.class ).in( Singleton.class );
        bind( EntityEncoder.class ).in( Singleton.class );
        bind( EntityDecoder.class ).in( Singleton.class );
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

        requestStaticInjection( KeyProvider.class );
        requestStaticInjection( BaseMetadata.class );
        requestStaticInjection( BaseMetadataItem.class );
        requestStaticInjection( IAMAuthenticator.class );
        requestStaticInjection( ImportBatchTask.class );
        requestStaticInjection( MetadataItemKey.class );
    }

    @Provides
    @Singleton
    // TODO: drop
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
    public Datastore provideDatastore()
    {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Provides
    @Singleton
    public Storage provideStorage()
    {
        return StorageOptions.getDefaultInstance().getService();
    }

    @Provides
    @Singleton
    public MapperFacade provideMapperFacade( MapperFactory factory,
                                             // mappers
                                             ChangeSetEntityToEntityBuilderMapper changeSetEntityToEntityBuilderMapper,
                                             ImportToImportMetadataMapper importToImportMetadataMapper,
                                             ExportToExportMetadataMapper exportToExportMetadataMapper,
                                             ChangeToChangeMetadataMapper changeToChangeMetadataMapper,

                                             ImportItemToImportMetadataItemMapper importItemToImportMetadataItemMapper,
                                             ChangeItemToChangeMetadataItemMapper changeItemToChangeMetadataItemMapper,

                                             // factories
                                             EntityBuilderFactory entityBuilderFactory,
                                             ImportMetadataFactory importMetadataFactory,
                                             ExportMetadataFactory exportMetadataFactory,
                                             ChangeMetadataFactory changeMetadataFactory )
    {
        // register custom mappers
        factory.registerMapper( changeSetEntityToEntityBuilderMapper );
        factory.registerMapper( importToImportMetadataMapper );
        factory.registerMapper( exportToExportMetadataMapper );
        factory.registerMapper( changeToChangeMetadataMapper );

        factory.registerMapper( importItemToImportMetadataItemMapper );
        factory.registerMapper( changeItemToChangeMetadataItemMapper );

        // register factories
        factory.registerObjectFactory( entityBuilderFactory, TypeFactory.valueOf( Entity.Builder.class ) );
        factory.registerObjectFactory( importMetadataFactory, TypeFactory.valueOf( ImportMetadata.class ) );
        factory.registerObjectFactory( exportMetadataFactory, TypeFactory.valueOf( ExportMetadata.class ) );
        factory.registerObjectFactory( changeMetadataFactory, TypeFactory.valueOf( ChangeMetadata.class ) );

        return factory.getMapperFacade();
    }

    @Provides
    @Singleton
    @BucketName
    public String provideBucketName()
    {
        return "ctoolkit-agent-morty.appspot.com";
        // TODO: resolve
//        return ServiceOptions.getDefaultProjectId(); // same as default project id
    }

    @Provides
    @Singleton
    @StagingLocation
    public String provideStagingLocation( @BucketName String bucketName )
    {
        return "gs://staging." + bucketName + "/dataflow-staging";
    }

    @Provides
    @Singleton
    @ProjectId
    public String provideProjectId()
    {
        return ServiceOptions.getDefaultProjectId();
    }

    @Provides
    public PipelineOptions providePipelineOptions( @ProjectId String projectId, @StagingLocation String stagingLocation )
    {
        if ( SystemProperty.environment.value() == SystemProperty.Environment.Value.Production || true ) // TODO: revert '|| true'
        {
            // The app is running on App Engine...
            DataflowPipelineOptions options = PipelineOptionsFactory.create().as( DataflowPipelineOptions.class );
            options.setRunner( DataflowPipelineRunner.class );
            options.setProject( projectId );
            options.setStagingLocation( stagingLocation );

            return options;
        }
        else
        {
            return PipelineOptionsFactory.create();
        }
    }

    @Provides
    @Singleton
    public Dataflow provideDataflow( PipelineOptions pipelineOptions )
    {
        if ( pipelineOptions instanceof DataflowPipelineOptions )
        {
            return ( ( DataflowPipelineOptions ) pipelineOptions ).getDataflowClient();
        }
        return null;
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

    static class JsonAuthKeyProvider
            implements AuthKeyProvider
    {
        private PropertyService propertyService;

        private String json;

        @Inject
        JsonAuthKeyProvider()
        {
        }

        @Override
        public InputStream get( @Nullable String prefix )
        {
            if ( json != null && CtoolkitApiAgentModule.API_PREFIX.equals( prefix ) )
            {
                return new ByteArrayInputStream( json.getBytes( Charsets.UTF_8 ) );
            }
            throw new IllegalArgumentException( "JSON credential provider is not configured for '" + prefix + "'" );
        }

        @Override
        public boolean isConfigured( @Nullable String prefix )
        {
            if ( !Strings.isNullOrEmpty( json ) )
            {
                // already configured
                return true;
            }
            if ( CtoolkitApiAgentModule.API_PREFIX.equals( prefix ) )
            {
                json = propertyService.getString( AgentModule.CONFIG_JSON_CREDENTIALS );
                if ( json == null )
                {
                    throw new NullPointerException( "Missing JSON credential for '" + prefix + "'" );
                }
            }
            return json != null;
        }
    }
}
