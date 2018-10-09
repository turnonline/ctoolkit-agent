package org.ctoolkit.agent.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.ctoolkit.agent.converter.ConverterExecutor;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link WorkerService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class WorkerServiceBean
        implements WorkerService
{
    private static final Logger log = LoggerFactory.getLogger( WorkerServiceBean.class );

    @Inject
    private MongoClient mongoClient;

    @Inject
    private ConverterExecutor converterExecutor;

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        // TODO: implement
        return null;
    }

    public List<EntityExportData> retrieveEntityMetaDataList( String sql )
    {
        // TODO: implement
        return null;
    }

    @Override
    public void importData( ImportSet importSet )
    {
        // delete database if requested
        if ( importSet.getClean() )
        {
            deleteCollection( importSet );
        }

        // import if namespace, kind and id is specified
        if ( importSet.getNamespace() != null && importSet.getKind() != null && importSet.getId() != null )
        {
            createIndex( importSet );
        }
    }

    // -- private helpers

    private void createIndex( ImportSet importSet )
    {
        String collectionName = importSet.getKind();

        try
        {
            Document document = new Document();

            // set id if provided
            String id = importSet.getId();
            if (id != null)
            {
                document.append( "_id", id );
            }

            for ( ImportSetProperty property : importSet.getProperties() )
            {
                addProperty( property.getName(), property, document );
            }

            // get database
            MongoDatabase database = mongoClient.getDatabase( importSet.getNamespace() );

            // get collection
            MongoCollection<Document> collection = database.getCollection( collectionName );

            // insert document
            collection.insertOne( document );
        }
        catch ( MongoException e )
        {
            log.error( "Unable to write document: " + importSet.getNamespace() + ":" + collectionName, e );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void addProperty( String name, ImportSetProperty importSetProperty, Map<String, Object> jsonMap )
    {
        // check if property is nested (i.e. identification.simple.value)
        LinkedList<String> subNames = new LinkedList<>( Arrays.asList( name.split( "\\." ) ) );
        if ( subNames.size() > 1 )
        {
            String nestedName = subNames.removeFirst();
            Map<String, Object> nestedMap = ( HashMap<String, Object> ) jsonMap.get( nestedName );
            if ( nestedMap == null )
            {
                nestedMap = new HashMap<>();
                jsonMap.put( nestedName, nestedMap );
            }

            // construct new name
            StringBuilder newName = new StringBuilder();
            subNames.forEach( s -> {
                if ( newName.length() > 0 )
                {
                    newName.append( "." );
                }
                newName.append( s );
            } );

            // recursive call to sub name
            addProperty( newName.toString(), importSetProperty, nestedMap );
        }
        else
        {
            Object convertedValue = converterExecutor.convertProperty( importSetProperty );
            jsonMap.put( name, convertedValue );
        }
    }

    private void deleteCollection( ImportSet importSet )
    {
        mongoClient.getDatabase( importSet.getNamespace() ).getCollection( importSet.getKind() ).drop();;
    }
}
