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

import com.google.api.client.util.Base64;
import com.google.appengine.api.datastore.Entity;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.restapi.client.Identifier;
import org.ctoolkit.restapi.client.RequestCredential;
import org.ctoolkit.restapi.client.ResourceFacade;
import org.ctoolkit.restapi.client.agent.model.DataType;
import org.ctoolkit.restapi.client.agent.model.ImportBatch.ImportItem;

import javax.inject.Inject;

/**
 * Datastore implementation of migration job
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrateMapOnlyMapperJob
        extends BatchMapOnlyMapperJob
{
    @Inject
    private transient ResourceFacade facade;

    @Override
    public void map( Entity item )
    {
        super.map( item );

        // get item property values
        String name = ( String ) item.getProperty( "name" );
        byte[] data = storageService.read( bucketName, ( String ) item.getProperty( "fileName" ) );
        DataType dataType = DataType.valueOf( ( String ) item.getProperty( "dataType" ) );

        // load parent to retrieve context properties
//        ExportMetadata exportMetadata = changeSetService.get( new MetadataKey<>(
//                KeyFactory.keyToString( item.getParent() ), ExportMetadata.class ) );

        ExportMetadata exportMetadata = null;
        String gtoken = exportMetadata.getJobContext().get( "gtoken" );
        String rootUrl = exportMetadata.getJobContext().get( "rootUrl" );
        String importKey = exportMetadata.getJobContext().get( "importKey" );

        RequestCredential credential = new RequestCredential();
        credential.setApiKey( gtoken );
        credential.setEndpointUrl( rootUrl );

        // create new import item
        ImportItem importItem = new ImportItem();
        importItem.setName( name );
        importItem.setDataType( dataType );
        importItem.setData( Base64.encodeBase64String( data ) );

        // call remote agent
        try
        {
            // parent import key as identifier
            facade.insert( importItem, new Identifier( importKey ) ).config( credential ).execute();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Unable to create import item", e );
        }
    }
}
