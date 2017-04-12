package org.ctoolkit.agent.service.impl.hadoop;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MetadataItemInputFormat
        extends InputFormat<Key, Entity>
{
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static String PARAM__PARENT_KEY = "param.parentKey";

    @Override
    public List<org.apache.hadoop.mapreduce.InputSplit> getSplits( JobContext jobContext )
            throws IOException, InterruptedException
    {
        String parentKeyString = jobContext.getConfiguration().get( PARAM__PARENT_KEY );
        List<Key> keys = getKeys( KeyFactory.stringToKey( parentKeyString ) );

        List<org.apache.hadoop.mapreduce.InputSplit> splits = new ArrayList<>( keys.size() );
        for ( Key key : keys )
        {
            splits.add( new MetadataItemInputSplit( KeyFactory.keyToString( key ) ) );
        }

        return splits;
    }

    @Override
    public org.apache.hadoop.mapreduce.RecordReader<Key, Entity> createRecordReader( org.apache.hadoop.mapreduce.InputSplit inputSplit, TaskAttemptContext taskAttemptContext )
            throws IOException, InterruptedException
    {
        return new MetadataItemRecordReader( ( MetadataItemInputSplit ) inputSplit );
    }

    // -- private helpers

    @SuppressWarnings( "unchecked" )
    private List<Key> getKeys( Key parentKey )
    {
        try
        {
            Entity parent = datastore.get( parentKey );
            return ( List<Key> ) parent.getProperty( "itemsRef" );
        }
        catch ( EntityNotFoundException e )
        {
            throw new IllegalArgumentException( "Unable to load parent key: " + parentKey, e );
        }
    }

    // -- private classes

    private class MetadataItemInputSplit
            extends org.apache.hadoop.mapreduce.InputSplit
    {
        private String itemKey;

        public MetadataItemInputSplit( String itemKey )
        {
            this.itemKey = itemKey;
        }

        @Override
        public long getLength() throws IOException
        {
            return 1;
        }

        @Override
        public String[] getLocations() throws IOException
        {
            return new String[0];
        }

        public String getItemKey()
        {
            return itemKey;
        }
    }

    private class MetadataItemRecordReader
            extends org.apache.hadoop.mapreduce.RecordReader<Key, Entity>
    {
        private MetadataItemInputSplit inputSplit;

        public MetadataItemRecordReader( MetadataItemInputSplit inputSplit )
        {
            this.inputSplit = inputSplit;
        }

        @Override
        public void close() throws IOException
        {

        }

        @Override
        public void initialize( org.apache.hadoop.mapreduce.InputSplit inputSplit, TaskAttemptContext taskAttemptContext )
                throws IOException, InterruptedException
        {

        }

        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException
        {
            return false;
        }

        @Override
        public Key getCurrentKey() throws IOException, InterruptedException
        {
            return KeyFactory.stringToKey( inputSplit.getItemKey() );
        }

        @Override
        public Entity getCurrentValue() throws IOException, InterruptedException
        {
            try
            {
                return datastore.get( getCurrentKey() );
            }
            catch ( EntityNotFoundException e )
            {
                throw new RuntimeException( "Entity not found for key: " + inputSplit.getItemKey(), e );
            }
        }

        @Override
        public float getProgress() throws IOException
        {
            return 0;
        }
    }
}
