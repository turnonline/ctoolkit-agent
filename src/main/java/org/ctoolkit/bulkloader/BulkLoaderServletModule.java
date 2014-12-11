package org.ctoolkit.bulkloader;

import com.google.inject.servlet.ServletModule;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderServletModule
        extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        serve( "/tasks/bulk-loader" ).with( BulkLoaderJobServlet.class );
//        filter( "/*" ).through( NamespaceFilter.class );
    }
}
