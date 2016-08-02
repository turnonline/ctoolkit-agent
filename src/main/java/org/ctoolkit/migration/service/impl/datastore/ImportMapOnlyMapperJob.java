package org.ctoolkit.migration.service.impl.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.inject.Injector;
import org.ctoolkit.migration.model.ChangeSet;
import org.ctoolkit.migration.model.JobState;
import org.ctoolkit.migration.service.ChangeSetService;
import org.ctoolkit.migration.util.XmlUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;

/**
 * Datastore implementation of import job
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportMapOnlyMapperJob
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

        Blob xml = ( Blob ) item.getProperty( "xml" );

        ChangeSet changeSet = XmlUtils.unmarshall( new ByteArrayInputStream( xml.getBytes() ), ChangeSet.class );
        changeSetService.importChangeSet( changeSet );

        // update state to COMPLETED_SUCCESSFULLY
        item.setProperty( "state", JobState.COMPLETED_SUCCESSFULLY.name() );
        datastoreService.put( item );
    }
}
