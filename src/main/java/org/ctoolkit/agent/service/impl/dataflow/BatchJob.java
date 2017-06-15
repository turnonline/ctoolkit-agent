package org.ctoolkit.agent.service.impl.dataflow;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.agent.service.impl.datastore.ShardedCounter;
import org.ctoolkit.services.storage.StorageService;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public abstract class BatchJob
{
    @Inject
    protected ChangeSetService changeSetService;

    @Inject
    protected DatastoreService datastoreService;

    @Inject
    protected StorageService storageService;

    @Inject
    @BucketName
    protected String bucketName;

    public abstract void doJob( Entity value );

    protected void updateParent( final Entity item, final JobState jobState )
    {
        Key parentKey = item.getParent();

        int shardCount = 10; // TODO: what value?

        if ( jobState == JobState.COMPLETED_SUCCESSFULLY )
        {
            ShardedCounter.okCounter( parentKey.getKind(), parentKey.getId(), shardCount ).increment();
        }
        else
        {
            ShardedCounter.errorCounter( parentKey.getKind(), parentKey.getId(), shardCount ).increment();
        }
    }
}
