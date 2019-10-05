/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.service.sql;

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
        Connection connection = null;
        PreparedStatement statement = null;

        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement( sql );

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
        finally
        {
            if ( statement != null )
            {
                try
                {
                    statement.close();
                }
                catch ( SQLException e )
                {
                    log.error( "Unable to close statement", e );
                }
            }
            if ( connection != null )
            {
                try
                {
                    connection.close();
                }
                catch ( SQLException e )
                {
                    log.error( "Unable to close connection", e );
                }
            }
        }
    }

    public abstract void process( ResultSet resultSet ) throws SQLException;
}
