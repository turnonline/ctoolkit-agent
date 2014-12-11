package org.ctoolkit.bulkloader;

import com.comvai.services.multitenancy.MultitenancyManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Singleton
public class NamespaceFilter
        implements Filter
{
    private static String DEMO_NAMESPACE = "ComvaiDEMO";

    private final MultitenancyManager manager;

    @Inject
    public NamespaceFilter( MultitenancyManager manager )
    {
        this.manager = manager;
    }

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException, ServletException
    {
        if ( manager.get() == null )
        {
            manager.set( DEMO_NAMESPACE );
        }

        // chain into the next request
        chain.doFilter( request, response );
    }

    @Override
    public void destroy()
    {
    }
}
