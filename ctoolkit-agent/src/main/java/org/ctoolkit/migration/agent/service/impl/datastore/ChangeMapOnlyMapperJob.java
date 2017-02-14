package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.common.base.Charsets;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.util.XmlUtils;

import java.io.ByteArrayInputStream;

/**
 * Datastore implementation of change job
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ChangeMapOnlyMapperJob
        extends BatchMapOnlyMapperJob
{
    @Override
    public void map( Entity item )
    {
        super.map( item );

        byte[] data = storageService.serve( ( String ) item.getProperty( "fileName" ), bucketName );
        ISetItem.DataType dataType = ISetItem.DataType.valueOf( ( String ) item.getProperty( "dataType" ) );
        ChangeSet changeSet;

        // TODO: load data

        switch ( dataType )
        {
            case JSON:
            {
                changeSet = new Gson().fromJson( new String( data, Charsets.UTF_8 ), ChangeSet.class );
                break;
            }
            case XML:
            {
                changeSet = XmlUtils.unmarshall( new ByteArrayInputStream( data ), ChangeSet.class );
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

        // update process for parent
        updateParent( item, jobState );
    }
}
