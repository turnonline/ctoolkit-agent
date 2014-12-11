package org.ctoolkit.bulkloader.conf;

import com.comvai.gwt.ApplicationModule;
import com.comvai.gwt.ApplicationServletModule;
import com.comvai.services.gae.CommonServicesGaeModule;
import com.comvai.services.gae.CommonServicesGaeServletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.ctoolkit.bulkloader.BulkLoaderModule;
import org.ctoolkit.bulkloader.BulkLoaderServletModule;
import org.ctoolkit.bulkloader.facade.CommandRemoteServlet;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class GuiceServletConfig
        extends com.google.inject.servlet.GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( Stage.DEVELOPMENT, new BulkLoaderModule(),
                new BulkLoaderServletModule(),
                new CommonServicesGaeModule(),
                new CommonServicesGaeServletModule(),
                new ApplicationModule(),
                new ApplicationServletModule( "bulkloader", CommandRemoteServlet.class ) );
    }
}
