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

package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.inject.Injector;
import org.ctoolkit.agent.annotation.BucketName;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.service.ChangeSetService;
import org.ctoolkit.services.storage.StorageService;

import javax.inject.Inject;

/**
 * Base mapper job for batch based jobs
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class BatchMapOnlyMapperJob
        extends MapOnlyMapper<Entity, Entity>
{
    @Inject
    protected static Injector injector;

    @Inject
    protected transient ChangeSetService changeSetService;

    @Inject
    protected transient DatastoreService datastoreService;

//    @Inject
    protected transient StorageService storageService;

    @Inject
    @BucketName
    protected transient String bucketName;

    @Override
    public void map( Entity value )
    {
        injector.injectMembers( this );
    }

    protected void updateParent( final Entity item, final JobState jobState )
    {
        Key parentKey = item.getParent();

        int shardCount = getContext().getShardCount() * 5;

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
