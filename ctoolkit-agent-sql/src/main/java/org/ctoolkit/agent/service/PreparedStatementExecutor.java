package org.ctoolkit.agent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public abstract class PreparedStatementExecutor
{
    private static final Logger log = LoggerFactory.getLogger( PreparedStatementExecutor.class );

    private final DataSource dataSource;

    private final String sql;

    public PreparedStatementExecutor( DataSource dataSource, String sql )
    {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public void execute()
    {
        try (Connection connection = dataSource.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement( sql );
            boolean executed = statement.execute();
            if ( executed )
            {
                ResultSet resultSet = statement.getResultSet();
                process( resultSet );
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
    }

    public abstract void process( ResultSet resultSet ) throws SQLException;
}
