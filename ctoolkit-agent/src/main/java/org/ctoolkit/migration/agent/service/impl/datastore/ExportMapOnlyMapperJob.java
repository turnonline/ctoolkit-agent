package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.common.base.Charsets;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.JobState;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;
import org.ctoolkit.migration.agent.util.XmlUtils;

/**
 * Datastore implementation of export job
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
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
            jobState = JobState.STOPPED_BY_ERROR;
        }

        // update state to COMPLETED_SUCCESSFULLY
        item.setProperty( "state", jobState.name() );
        item.setProperty( "data", new Blob( data != null ? data.getBytes( Charsets.UTF_8 ) : null ) );
        datastoreService.put( item );

        // update process for parent
        updateParent( item, jobState );
    }
}
