package org.ctoolkit.agent.beam;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

/**
 * Jdbc pipeline options
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface JdbcPipelineOptions extends PipelineOptions
{
    @Description( "JDBC datasource url" )
    String getJdbcUrl();
    void setJdbcUrl( String url );

    @Description( "JDBC datasource username" )
    String getJdbcUsername();
    void setJdbcUsername( String username );

    @Description( "JDBC datasource password" )
    String getJdbcPassword();
    void setJdbcPassword( String password );

    @Description( "JDBC datasource driver" )
    String getJdbcDriver();
    void setJdbcDriver( String driver );
}
