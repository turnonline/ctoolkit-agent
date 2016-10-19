package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import com.google.common.base.Charsets;
import com.google.inject.Injector;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.JobInfoMessage;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.service.ChangeSetService;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.util.XmlUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;

/**
 * Datastore implementation of change job
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeMapOnlyMapperJob
        extends MapOnlyMapper<Entity, Entity>
{
    @Inject
    private static Injector injector;

    @Inject
    private transient ChangeSetService changeSetService;

    @Inject
    private transient DatastoreService datastoreService;

    @Inject
    private transient ChannelService channelService;

    @Override
    public void map( Entity item )
    {
        injector.injectMembers( this );

        String clientId = KeyFactory.keyToString( item.getKey().getParent() );
        Blob data = ( Blob ) item.getProperty( "data" );
        ISetItem.DataType dataType = ISetItem.DataType.valueOf( ( String ) item.getProperty( "dataType" ) );
        ChangeSet changeSet;

        switch ( dataType )
        {
            case JSON:
            {
                changeSet = new Gson().fromJson( new String( data.getBytes(), Charsets.UTF_8 ), ChangeSet.class );
                break;
            }
            case XML:
            {
                changeSet = XmlUtils.unmarshall( new ByteArrayInputStream( data.getBytes() ), ChangeSet.class );
                break;
            }
            default:
            {
                throw new IllegalArgumentException( "Unknown data type: '" + dataType + "'" );
            }
        }

        JobState jobState;

        try
        {
            changeSetService.changeChangeSet( changeSet );
            jobState = JobState.COMPLETED_SUCCESSFULLY;
        }
        catch ( Exception e )
        {
            jobState = JobState.STOPPED_BY_ERROR;
        }

        // update state to COMPLETED_SUCCESSFULLY
        item.setProperty( "state", jobState.name() );
        datastoreService.put( item );

        // send channel message to client
        JobInfoMessage message = new JobInfoMessage();
        message.setKey( KeyFactory.keyToString( item.getKey() ) );
        message.setStatus( JobInfoMessage.Status.valueOf( jobState.name() ) );

        channelService.sendMessage( new ChannelMessage( clientId, new Gson().toJson( message ) ) );
    }
}
