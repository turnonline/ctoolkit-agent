package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.inject.Injector;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Datastore implementation of change job
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ExportMapOnlyMapperJob
        extends MapOnlyMapper<Entity, Entity>
{
    @Inject
    private static Injector injector;

    @Inject
    private transient ChangeSetService changeSetService;

    @Inject
    private transient DatastoreService datastoreService;

    @Override
    public void map( Entity item )
    {
        injector.injectMembers( this );

        String entityToExport = ( String ) item.getProperty( "entityToExport" );
        byte[] xml = changeSetService.exportChangeSet( entityToExport );

        // update state to COMPLETED_SUCCESSFULLY
        item.setProperty( "state", JobState.COMPLETED_SUCCESSFULLY.name() );
        item.setProperty( "xml", new Blob( xml ) );
        datastoreService.put( item );
    }
}
