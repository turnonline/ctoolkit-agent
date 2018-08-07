package org.ctoolkit.agent.beam;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.io.Serializable;
import java.util.List;

/**
 * Implementation of {@link DoFnFactory}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Factory
public class DoFnFactoryBean
        implements DoFnFactory, Serializable
{
    @Bean
    @Override
    public DoFn<MigrationSet, KV<MigrationSet, String>> createSplitQueriesDoFn()
    {
        return new SplitQueriesDoFn();
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, String>, KV<MigrationSet, List<EntityMetaData>>> createRetrieveEntityMetadataListDoFn()
    {
        return new RetrieveEntityMetaDataDoFn();
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, List<EntityMetaData>>, Void> createTransformAndImportDoFn()
    {
        return new TransformAndImportDoFn();
    }
}
