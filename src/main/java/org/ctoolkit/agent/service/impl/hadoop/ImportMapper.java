package org.ctoolkit.agent.service.impl.hadoop;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportMapper extends Mapper<Key, Entity, Void, Void>
{
    @Override
    protected void map( Key key, Entity value, Context context ) throws IOException, InterruptedException
    {
        System.out.println( "Key: " + key );
        System.out.println( "Entity: " + value );
    }
}
