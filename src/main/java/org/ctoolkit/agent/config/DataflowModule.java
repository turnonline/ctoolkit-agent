package org.ctoolkit.agent.config;

import com.google.api.services.dataflow.Dataflow;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.annotation.ProjectId;
import org.ctoolkit.agent.annotation.StagingLocation;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.RestContext;
import org.ctoolkit.agent.service.impl.ChangeSetServiceBean;
import org.ctoolkit.agent.service.impl.RestContextThreadLocal;
import org.ctoolkit.agent.service.impl.datastore.EntityPool;
import org.ctoolkit.agent.service.impl.datastore.EntityPoolThreadLocal;
import org.ctoolkit.agent.service.impl.mapper.ChangeSetEntityToEntityBuilderMapper;
import org.ctoolkit.agent.service.impl.mapper.EntityBuilderFactory;

import javax.inject.Singleton;

/**
 * Module used in dataflow jobs. It is a lightweight version of {@link AgentModule}
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class DataflowModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind( EntityPool.class ).to( EntityPoolThreadLocal.class ).in( Singleton.class );
        bind( ChangeSetService.class ).to( ChangeSetServiceBean.class ).in( Singleton.class );
        bind( RestContext.class ).to( RestContextThreadLocal.class ).in( Singleton.class );

        install( new MigrationModule() );
        install( new StorageModule() );

        requestStaticInjection( BaseMetadata.class );
        requestStaticInjection( BaseMetadataItem.class );
    }

    @Provides
    @Singleton
    @BucketName
    public String provideBucketName()
    {
        return ServiceOptions.getDefaultProjectId() + ".appspot.com";
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
                                             ChangeSetEntityToEntityBuilderMapper changeSetEntityToEntityBuilderMapper,

                                             // factories
                                             EntityBuilderFactory entityBuilderFactory )
    {
        // register custom mappers
        factory.registerMapper( changeSetEntityToEntityBuilderMapper );

        // register factories
        factory.registerObjectFactory( entityBuilderFactory, TypeFactory.valueOf( Entity.Builder.class ) );

        return factory.getMapperFacade();
    }

    @Provides
    public PipelineOptions providePipelineOptions( @ProjectId String projectId, @StagingLocation String stagingLocation )
    {
        DataflowPipelineOptions options = PipelineOptionsFactory.create().as( DataflowPipelineOptions.class );
        options.setRunner( DataflowRunner.class );
        options.setProject( projectId );
        options.setStagingLocation( stagingLocation );

        return options;
    }

    @Provides
    @Singleton
    public Dataflow provideDataflow(PipelineOptions pipelineOptions )
    {
        if ( pipelineOptions instanceof DataflowPipelineOptions )
        {
            return ( ( DataflowPipelineOptions ) pipelineOptions ).getDataflowClient();
        }

        return null;
    }
}
