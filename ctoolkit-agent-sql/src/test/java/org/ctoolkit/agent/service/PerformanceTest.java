package org.ctoolkit.agent.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Ignore
public class PerformanceTest
{
    private PGSimpleDataSource dataSource;

    @Before
    public void setUp() throws Exception
    {
        dataSource = new PGSimpleDataSource();
        dataSource.setUrl( "jdbc:postgresql://morty:5432/root" );
        dataSource.setUser( "root" );
        dataSource.setPassword( "admin123" );
    }

    @Test
    public void prepareData() throws Exception
    {
        Connection connection = null;

        try
        {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            int rows = 10000;

            for ( int i = 1; i <= rows; i++ )
            {
                statement.execute( "INSERT INTO global.PersData (id, name, surname, sexType) VALUES (" + i + ", 'John', 'Foo', 'M');" );
                if (i % 1000 == 0) {
                    System.out.println("Iteration: " + i);
                }
            }
        }
        finally
        {
            if ( connection != null )
            {
                connection.close();
            }
        }
    }
}
