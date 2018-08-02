package org.ctoolkit.agent.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.util.SelectUtils;
import org.ctoolkit.agent.beam.MigrationPipelineOptions;
import org.ctoolkit.agent.model.CountColumn;
import org.ctoolkit.agent.model.EntityMetaData;
import org.ctoolkit.agent.model.VendorIndependentLimit;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<String> splitQueries( MigrationSet migrationSet, int rowsPerSplit )
    {
        List<String> queries = new ArrayList<>();
        String query = migrationSet.getQuery();
        Select rootSelect;
        Select rootCountSelect;

        // create select as 'select * from sourceNamespace.sourceKind'
        if ( query == null )
        {
            Table table = new Table( migrationSet.getSourceNamespace(), migrationSet.getSourceKind() );
            rootSelect = SelectUtils.buildSelectFromTable( table );
            rootCountSelect = SelectUtils.buildSelectFromTable( table );
        }
        // create select as provided by query in MigrationSet
        else
        {
            try
            {
                rootSelect = ( Select ) CCJSqlParserUtil.parse( query );
                rootCountSelect = ( Select ) CCJSqlParserUtil.parse( query );
            }
            catch ( JSQLParserException e )
            {
                log.error( "Unable to parse root query: " + query, e );
                throw new RuntimeException( "Unable to parse root query: " + query, e );
            }
        }

        // replace select items with 'select count(*) ...'
        rootCountSelect.getSelectBody().accept( new SelectVisitorAdapter()
        {
            @Override
            public void visit( PlainSelect plainSelect )
            {
                plainSelect.setSelectItems( Collections.singletonList( new CountColumn() ) );
            }
        } );

        // get split numbers
        String rootCountQuery = rootCountSelect.toString();
        PreparedStatementExecutor executor = new PreparedStatementExecutor( dataSource, rootCountQuery )
        {
            @Override
            public void process( ResultSet resultSet ) throws SQLException
            {
                while ( resultSet.next() )
                {
                    int count = resultSet.getInt( 1 );
                    // wee ned to split query into multiple offset + limit queries
                    if ( count > rowsPerSplit )
                    {
                        BigDecimal splits = BigDecimal.valueOf( count ).divide( BigDecimal.valueOf( rowsPerSplit ), RoundingMode.UP );

                        for ( int offset = 0; offset < splits.doubleValue(); offset++ )
                        {
                            // create offset + limit per split
                            rootSelect.getSelectBody().accept( new VendorIndependentLimit( offset, rowsPerSplit ) );
                            queries.add( rootSelect.toString() );
                        }
                    }
                    // noo need to split query, because there is less rows then ROWS_PER_SPLIT
                    else
                    {
                        queries.add( rootSelect.toString() );
                    }
                }
            }
        };
        executor.execute();

        return queries;
    }

    public List<EntityMetaData> retrieveEntityMetaDataList( String sql )
    {
        List<EntityMetaData> entityMetaDataList = new ArrayList<>();

        PreparedStatementExecutor executor = new PreparedStatementExecutor( dataSource, sql )
        {
            @Override
            public void process( ResultSet resultSet ) throws SQLException
            {
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
        };
        executor.execute();

        return entityMetaDataList;
    }

    public void migrate( MigrationSet migrationSet, List<EntityMetaData> entityMetaDataList )
    {
        for ( EntityMetaData entityMetaData : entityMetaDataList )
        {
            log.info( "Migrate: " + entityMetaData );
        }

        // TODO: implement
    }
}
