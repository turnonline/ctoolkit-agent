package org.ctoolkit.agent.service.impl.dataflow.shared;

import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.PTransform;
import com.google.cloud.dataflow.sdk.values.PBegin;
import com.google.cloud.dataflow.sdk.values.PCollection;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyValue;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.ModelConverter;

/**
 * PTransform for loading BaseMetadata items
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class LoadItems<M extends BaseMetadata<?>>
        extends PTransform<PBegin, PCollection<KeyValue>>
{
    private Datastore datastore;

    private Key key;

    private Class<M> clazz;

    public LoadItems( Key key, Class<M> clazz, Datastore datastore )
    {
        this.key = key;
        this.clazz = clazz;
        this.datastore = datastore;
    }

    @Override
    public PCollection<KeyValue> apply( PBegin input )
    {
        Entity entity = datastore.get( key );
        M metadata = ModelConverter.convert( clazz, entity );

        return input.apply( Create.of( metadata.getItemsKeyValue() ) );
    }
}