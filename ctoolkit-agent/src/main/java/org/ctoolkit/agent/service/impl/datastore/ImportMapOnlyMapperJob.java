package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.ctoolkit.agent.model.ISetItem;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.shared.resources.ChangeSet;
import org.ctoolkit.agent.util.StackTraceResolver;
import org.ctoolkit.agent.util.XmlUtils;

import java.io.ByteArrayInputStream;

/**
 * Datastore implementation of import job
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ImportMapOnlyMapperJob
        extends BatchMapOnlyMapperJob
{
    @Override
    public void map( Entity item )
    {
        super.map( item );

        byte[] data = storageService.serve( ( String ) item.getProperty( "fileName" ), bucketName );
        ISetItem.DataType dataType = ISetItem.DataType.valueOf( ( String ) item.getProperty( "dataType" ) );
        ChangeSet changeSet;

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
        Text error = null;

        try
        {
            changeSetService.importChangeSet( changeSet );
            jobState = JobState.COMPLETED_SUCCESSFULLY;
        }
        catch ( Exception e )
        {
            error = new Text( StackTraceResolver.resolve( e ) );
            jobState = JobState.STOPPED_BY_ERROR;
        }

        // update state
        item.setUnindexedProperty( "error", error );
        item.setProperty( "state", jobState.name() );
        datastoreService.put( item );

        // update process for parent
        updateParent( item, jobState );
    }
}