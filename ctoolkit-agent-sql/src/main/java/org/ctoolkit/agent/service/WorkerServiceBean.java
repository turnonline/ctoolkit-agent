package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link WorkerService}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class WorkerServiceBean
        implements WorkerService
{
    @Inject
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger( WorkerServiceBean.class );

    @SuppressWarnings( "unchecked" )
    public WorkerServiceBean( MigrationPipelineOptions pipelineOptions )
    {
        ApplicationContext ctx = ApplicationContext.run( CollectionUtils.mapOf(
                "datasources.default.url", pipelineOptions.getJdbcUrl(),
                "datasources.default.username", pipelineOptions.getJdbcUsername(),
                "datasources.default.password", pipelineOptions.getJdbcPassword(),
                "datasources.default.driver", pipelineOptions.getJdbcDriver()
        ) );

        ctx.inject( this );
    }

    @Override
    public List<String> splitQueries( MigrationSet migrationSet )
    {
        List<String> queries = new ArrayList<>();

        if ( migrationSet.getQuery() == null )
        {
            // TODO: implement split query
            String rootQuery = MessageFormat.format( "select * from {0}.{1}", migrationSet.getSourceNamespace(), migrationSet.getSourceKind() );
            queries.add( rootQuery );
        }
        else
        {
            // TODO: implement split query
            String rootQuery = migrationSet.getQuery();
        }

        return queries;
    }

    @Override
    public List<EntityMetaData> retrieveEntityMetaDataList( String sql )
    {
        List<EntityMetaData> entityMetaDataList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement( sql );
            boolean executed = statement.execute();
            if ( executed )
            {
                ResultSet resultSet = statement.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();

                while ( resultSet.next() )
                {
                    EntityMetaData entityMetaData = new EntityMetaData();
                    entityMetaDataList.add( entityMetaData );

                    for ( int i = 1; i <= metaData.getColumnCount(); i++ )
                    {
                        EntityMetaData.Property property = new EntityMetaData.Property();
                        property.setValue( resultSet.getObject( i ) );
                        property.setClassName( metaData.getColumnClassName( i ) );
                        property.setTypeName( metaData.getColumnTypeName( i ) );

                        entityMetaData.getProperties().put( metaData.getColumnName( i ), property );
                    }
                }
            }
            else
            {
                log.error( "Unable to execute sql: " + sql );
            }
        }
        catch ( SQLException e )
        {
            log.error( "Error occur during creating database connection", e );
        }

        return entityMetaDataList;
    }

    @Override
    public void migrate( MigrationSet migrationSet, List<EntityMetaData> entityMetaDataList )
    {
        for ( EntityMetaData entityMetaData : entityMetaDataList )
        {
            log.info( "Migrate: " + entityMetaData );
        }

        // TODO: implement
    }
}
