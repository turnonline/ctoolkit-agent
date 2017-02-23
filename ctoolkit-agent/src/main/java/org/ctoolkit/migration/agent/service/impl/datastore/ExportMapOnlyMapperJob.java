package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.ctoolkit.migration.agent.model.BaseMetadataItem;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.util.StackTraceResolver;
import org.ctoolkit.migration.agent.util.XmlUtils;

/**
 * Datastore implementation of export job
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class ExportMapOnlyMapperJob
        extends BatchMapOnlyMapperJob
{
    @Override
    public void map( Entity item )
    {
        super.map( item );

        String entityToExport = ( String ) item.getProperty( "entityToExport" );
        ISetItem.DataType dataType = ISetItem.DataType.valueOf( ( String ) item.getProperty( "dataType" ) );
        String data = null;

        JobState jobState;
        Text error = null;

        try
        {
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
        item.setProperty( "dataLength", data != null ? ( long ) data.length() : 0L );
        datastoreService.put( item );

        // store blob
        if ( data != null )
        {
            storageService.store(
                    data.getBytes( Charsets.UTF_8 ),
                    dataType.mimeType(),
                    BaseMetadataItem.newFileName( KeyFactory.keyToString( item.getKey() ) ),
                    bucketName
            );
        }

        // update process for parent
        updateParent( item, jobState );
    }
}
