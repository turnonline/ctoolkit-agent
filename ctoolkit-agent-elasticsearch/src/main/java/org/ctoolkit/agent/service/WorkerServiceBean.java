package org.ctoolkit.agent.service;

import org.ctoolkit.agent.converter.ConverterExecutor;
import org.ctoolkit.agent.converter.ElasticsearchConverterRegistrat;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
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
    private RestHighLevelClient elasticClient;

    // TODO: unable to provide by ElasticsearchConfig
    @Inject
    private ConverterExecutor converterExecutor = new ConverterExecutor( new ElasticsearchConverterRegistrat() );

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
        // delete index if requested
        if ( importSet.getClean() )
        {
            deleteIndex( importSet );
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
        try
        {
            Map<String, Object> jsonMap = new HashMap<>();
            for ( ImportSetProperty property : importSet.getProperties() )
            {
                addProperty( property.getName(), property, jsonMap );
            }

            IndexRequest indexRequest = new IndexRequest( importSet.getNamespace(), importSet.getKind(), importSet.getId() );
            indexRequest.source( jsonMap );
            IndexResponse indexResponse = elasticClient.index( indexRequest );

            if ( indexResponse.status() != RestStatus.CREATED && indexResponse.status() != RestStatus.OK )
            {
                log.error( "Unexpected status. Expected: {},{} but was: {}", RestStatus.OK, RestStatus.CREATED, indexResponse.status() );
            }
        }
        catch ( IOException e )
        {
            log.error( "Unable to create index: " + importSet.getNamespace() + ":" + importSet.getKind(), e );
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

    private void deleteIndex( ImportSet importSet )
    {
        try
        {
            elasticClient.indices().delete( new DeleteIndexRequest( importSet.getNamespace() ) );
        }
        catch ( IOException e )
        {
            log.error( "Unable to delete index: " + importSet.getNamespace(), e );
        }
        catch ( ElasticsearchStatusException e )
        {
            if ( e.status() != RestStatus.NOT_FOUND )
            {
                log.error( "Unable to delete index.", e );
            }
        }
    }
}
