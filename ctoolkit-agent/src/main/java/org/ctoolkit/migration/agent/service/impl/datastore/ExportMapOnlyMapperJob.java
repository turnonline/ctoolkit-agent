package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.common.base.Charsets;
import com.google.inject.Injector;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.util.XmlUtils;

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
        ISetItem.DataType dataType = ( ISetItem.DataType ) item.getProperty( "dataType" );
        String data;

        ChangeSet changeSet = changeSetService.exportChangeSet( entityToExport );

        switch ( dataType )
        {
            case JSON:
            {
                data = new Gson().toJson( changeSet );
                break;
            }
            case XML:
            {
                data = XmlUtils.marshall( changeSet );
                break;
            }
            default:
            {
                throw new IllegalArgumentException( "Unknown data type: '" + dataType + "'" );
            }
        }

        // update state to COMPLETED_SUCCESSFULLY
        item.setProperty( "state", JobState.COMPLETED_SUCCESSFULLY.name() );
        item.setProperty( "data", new Blob( data.getBytes( Charsets.UTF_8 ) ) );
        datastoreService.put( item );
    }
}
