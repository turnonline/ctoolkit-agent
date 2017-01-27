package org.ctoolkit.migration.agent;

import com.google.api.client.http.HttpMethods;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AccessControlAllowOrignFilter
        implements Filter
{
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        // noop
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException, ServletException
    {
        HttpServletResponse httpResponse = ( HttpServletResponse ) response;
        HttpServletRequest httpRequest = ( HttpServletRequest ) request;

        httpResponse.setHeader( "Access-Control-Allow-Origin", "*" );
        httpResponse.setHeader( "Access-Control-Allow-Headers", "Access-Control-Allow-Origin, gtoken");

        if ( !httpRequest.getMethod().equals( HttpMethods.OPTIONS ) )
        {
            chain.doFilter( request, response );
        }
    }

    @Override
    public void destroy()
    {
        // noop
    }
}
