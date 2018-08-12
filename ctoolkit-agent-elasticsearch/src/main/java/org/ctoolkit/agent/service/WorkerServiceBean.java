package org.ctoolkit.agent.service;

import com.google.gson.Gson;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.ValueWithLabels;
import org.ctoolkit.agent.model.api.ImportSet;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

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

        // search by _sync property to set importSet id
        if ( importSet.getId() == null && importSet.getSyncId() != null )
        {
            SearchResponse searchResponse = searchDocument( importSet );

            if ( searchResponse != null )
            {
                SearchHits hits = searchResponse.getHits();
                if ( hits.getTotalHits() > 0 )
                {
                    // clone
                    Gson gson = new Gson();
                    importSet = gson.fromJson( gson.toJson( importSet ), ImportSet.class );

                    SearchHit hit = hits.getAt( 0 );
                    importSet.setId( hit.getId() );
                }
            }
        }

        // import if kind is specified
        if ( importSet.getKind() != null )
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
                jsonMap.put( property.getName(), property.getValue() );
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

    private SearchResponse searchDocument( ImportSet importSet )
    {
        ValueWithLabels value = ValueWithLabels.of( importSet.getSyncId() );
        String syncPropertyName = value.getLabels().getOrDefault( "name", "_syncId" );
        Object syncPropertyValue = value.getValue();

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.postFilter( matchQuery( syncPropertyName, syncPropertyValue ) );
        searchRequest.source( searchSourceBuilder );
        try
        {
            SearchResponse searchResponse = elasticClient.search( searchRequest );
            if ( searchResponse.status() != RestStatus.OK )
            {
                log.info( "Unexpected status. Expected: {} but was: {}", RestStatus.OK, searchResponse.status() );
            }
            else
            {
                return searchResponse;
            }
        }
        catch ( IOException e )
        {
            log.error( "Unable to search: " + importSet.getNamespace() + "." + importSet.getKind() +
                    " by " + syncPropertyName + ":" + syncPropertyValue, e );
        }

        return null;
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
