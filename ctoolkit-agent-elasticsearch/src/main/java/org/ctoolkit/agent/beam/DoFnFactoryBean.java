package org.ctoolkit.agent.beam;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
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
    public DoFn<ImportSet, Void> createImportDoFn()
    {
        return new ImportDoFn();
    }

    @Bean
    @Override
    public DoFn<MigrationSet, KV<MigrationSet, String>> createSplitQueriesDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, String>, KV<MigrationSet, List<EntityExportData>>> createRetrieveEntityMetadataListDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }

    @Bean
    @Override
    public DoFn<KV<MigrationSet, List<EntityExportData>>, Void> createTransformAndImportDoFn()
    {
        throw new RuntimeException( "Not implemented yet" );
    }
}
