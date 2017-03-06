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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ISetItem;
import org.ctoolkit.agent.model.JobState;
import org.ctoolkit.agent.shared.resources.ChangeSet;
import org.ctoolkit.agent.util.StackTraceResolver;
import org.ctoolkit.agent.util.XmlUtils;

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
