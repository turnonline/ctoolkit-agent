package org.ctoolkit.agent.service.impl.dataflow;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.ctoolkit.agent.model.ISetItem;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.util.StackTraceResolver;
import org.ctoolkit.agent.util.XmlUtils;

import java.io.ByteArrayInputStream;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportBatchJob
        extends BatchJob
{
    @Override
    public void doJob( Entity item )
    {
        byte[] data = storageService.read( bucketName, ( String ) item.getProperty( "fileName" ) );
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
            jobState = JobState.DONE;
        }
        catch ( Exception e )
        {
            error = new Text( StackTraceResolver.resolve( e ) );
            jobState = JobState.FAILED;
        }

        // update state
        item.setUnindexedProperty( "error", error );
        item.setProperty( "state", jobState.name() );
        datastoreService.put( item );

        // update process for parent
        updateParent( item, jobState );
    }
}
