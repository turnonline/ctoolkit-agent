package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.MigrationSet;

import java.util.List;

/**
 * {@link org.apache.beam.sdk.transforms.DoFn} factory
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface DoFnFactory
{
    DoFn<MigrationSet, KV<MigrationSet, String>> createSplitQueriesDoFn();

    DoFn<KV<MigrationSet, String>, KV<MigrationSet, List<EntityMetaData>>> createRetrieveEntityMetadataListDoFn();

    DoFn<KV<MigrationSet, List<EntityMetaData>>, Void> createTransformAndImportDoFn();
}
