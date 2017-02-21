package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.api.client.util.Base64;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import org.ctoolkit.api.migration.CtoolkitAgent;
import org.ctoolkit.api.migration.model.ImportItem;
import org.ctoolkit.migration.agent.config.CtoolkitAgentFactory;
import org.ctoolkit.migration.agent.model.CtoolkitAgentConfiguration;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.ISetItem;
import org.ctoolkit.migration.agent.model.MetadataKey;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Datastore implementation of migration job
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrateMapOnlyMapperJob
        extends BatchMapOnlyMapperJob
{
    @Inject
    private transient CtoolkitAgentFactory ctoolkitAgentFactory;

    @Override
    public void map( Entity item )
    {
        super.map( item );

        // get item property values
        String name = ( String ) item.getProperty( "name" );
        byte[] data = storageService.serve( ( String ) item.getProperty( "fileName" ), bucketName );
        ISetItem.DataType dataType = ISetItem.DataType.valueOf( ( String ) item.getProperty( "dataType" ) );

        // load parent to retrieve context properties
        ExportMetadata exportMetadata = changeSetService.get( new MetadataKey<>(
                KeyFactory.keyToString( item.getParent() ), ExportMetadata.class ) );

        String gtoken = exportMetadata.getJobContext().get( "gtoken" );
        String rootUrl = exportMetadata.getJobContext().get( "rootUrl" );
        String importKey = exportMetadata.getJobContext().get( "importKey" );

        // create new import item
        ImportItem importItem = new ImportItem();
        importItem.setName( name );
        importItem.setDataType( dataType.name() );
        importItem.setData( Base64.encodeBase64String( data ) );

        // call remote agent
        CtoolkitAgentConfiguration configuration = new CtoolkitAgentConfiguration( rootUrl, gtoken );
        CtoolkitAgent ctoolkitAgent = ctoolkitAgentFactory.provideCtoolkitAgent( configuration ).get();
        try
        {
            ctoolkitAgent.importBatch().item().insert( importKey, importItem ).execute();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Unable to create import item", e );
        }
    }
}
